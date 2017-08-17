package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.Details
import com.simagis.mrss.admin.web.ui.ptp.Result
import com.simagis.mrss.admin.web.ui.ptp.gridOf
import com.simagis.mrss.array
import com.simagis.mrss.json
import com.vaadin.annotations.Push
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.server.VaadinSession
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.*
import com.vaadin.ui.renderers.NumberRenderer
import com.vaadin.ui.renderers.Renderer
import com.vaadin.ui.themes.ValoTheme
import org.intellij.lang.annotations.Language
import java.text.DecimalFormat
import javax.servlet.annotation.WebServlet
import kotlin.concurrent.thread


/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 8/10/2017.
 */
private const val appCaption = "Test Panel Simulator (API version 0.5 - Prototype)"

@Title(appCaption)
@Push
class TestPanelSimulatorUI : UI() {

    private lateinit var contentMain: VerticalLayout

    override fun init(request: VaadinRequest) {
        contentMain = VerticalLayout().apply {
            setSizeFull()
            setMargin(false)
            isSpacing = false
            addComponent(HorizontalLayout().apply {
                setWidth(100f, Sizeable.Unit.PERCENTAGE)
                addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR)
                addComponent(Label(appCaption).apply {
                    addStyleName(ValoTheme.LABEL_COLORED)
                    addStyleName(ValoTheme.LABEL_LARGE)
                    addStyleName(ValoTheme.LABEL_BOLD)
                })
            })
            addComponentsAndExpand(TabSheet().apply {
                setSizeFull()
                addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR)
                val testDataProvider = ListDataProvider<ResultTestPay>(mutableSetOf())
                addTab(VerticalLayout().apply {
                    caption = "Test Composition"
                    val totals = Label("", ContentMode.HTML).apply {
                        setWidth(100f, Sizeable.Unit.PERCENTAGE)
                        addStyleName(ValoTheme.LABEL_BOLD)
                    }

                    fun updateTotals() {
                        //language=HTML
                        totals.value = """
                            <strong>Totals</strong>:
                                    Expect Fee - <strong>%s</strong>,
                                    Expected Value - <strong>%s</strong>"""
                                .format(
                                        dollarFormat.format(testDataProvider.items.sumByDouble { it.expectFee ?: 0.0 }),
                                        dollarFormat.format(testDataProvider.items.sumByDouble { it.expectValue ?: 0.0 })
                                )
                    }
                    updateTotals()

                    val similarPopupLayout = HorizontalLayout().apply {
                        setWidth(100f, Sizeable.Unit.PERCENTAGE)
                    }

                    addComponent(VerticalLayout().apply {
                        setHeightUndefined()
                        isSpacing = false
                        setMargin(false)
                        addComponent(totals)
                        addComponent(similarPopupLayout)
                    })

                    val grid = Grid<ResultTestPay>(ResultTestPay::class.java).apply {
                        setSizeFull()
                        dataProvider = testDataProvider
                        setSelectionMode(Grid.SelectionMode.MULTI)
                        setColumnOrder(
                                "test", "testName",
                                "expectFee",
                                "payProbability",
                                "expectValue",
                                "medicareFee"
                        )
                        with(getColumn("expectFee")) {
                            caption = "Expect Fee"
                            renderer = NumberRenderer(dollarFormat)
                            styleGenerator = StyleGenerator { "align-right" }
                        }
                        with(getColumn("payProbability")) {
                            caption = "Payment Probability"
                            styleGenerator = StyleGenerator { "align-right" }
                        }
                        with(getColumn("expectValue")) {
                            caption = "Expected Value"
                            renderer = NumberRenderer(dollarFormat)
                            styleGenerator = StyleGenerator { "align-right" }
                        }
                        with(getColumn("medicareFee")) {
                            caption = "Medicare Fee"
                            renderer = NumberRenderer(dollarFormat)
                            styleGenerator = StyleGenerator { "align-right" }
                        }

                        dataProvider.addDataProviderListener {
                            updateTotals()
                        }
                    }
                    addComponent(HorizontalLayout().apply {
                        setWidth(100f, Sizeable.Unit.PERCENTAGE)
                        setHeightUndefined()
                        fun rebuildSimilarPanelView() {
                            similarPopupLayout.removeAllComponents()
                            val tests = testDataProvider.items.map { it.test }.toSet()
                            if (tests.isEmpty()) return
                            similarPopupLayout.addComponent(ProgressBar().apply {
                                isIndeterminate = true
                            })
                            val ui = this.ui
                            thread(start = true) {
                                val similarPanelView = call_SimilarPanel(tests).toSimilarPanelView()
                                ui.access {
                                    similarPopupLayout.removeAllComponents()
                                    similarPopupLayout.addComponent(similarPanelView)
                                    similarPopupLayout.setComponentAlignment(similarPanelView, Alignment.MIDDLE_LEFT)
                                }
                            }
                        }

                        addComponentsAndExpand(ComboBox<ResultTestPay>().apply {
                            setItems(call_ResultTestPay().asList("ResultTestPay").map { ResultTestPay(it) })
                            placeholder = "Add Test"
                            popupWidth = "32em"
                            addSelectionListener {
                                value?.let {
                                    if (testDataProvider.items.add(it)) testDataProvider.refreshAll()
                                    grid.deselectAll()
                                    grid.select(it)
                                    rebuildSimilarPanelView()
                                }
                                value = null
                            }
                        })
                        addComponent(Button("Delete Selected", VaadinIcons.FILE_REMOVE).apply {
                            description = "Remove selected tests"
                            addClickListener {
                                testDataProvider.items.removeAll(grid.selectedItems)
                                testDataProvider.refreshAll()
                                rebuildSimilarPanelView()
                            }
                        })
                    })
                    addComponentsAndExpand(grid)
                    addComponent(Label("""These results are statistical estimate only.
                        Actual results will depend on combination of Insurance Payment Plans""").apply {
                        addStyleName(ValoTheme.LABEL_TINY)
                    })
                })
                val dxViewLayout = VerticalLayout().apply {
                    caption = "Diagnostic Codes"
                }
                addTab(dxViewLayout)

                addSelectedTabChangeListener {
                    if (selectedTab == dxViewLayout) {
                        dxViewLayout.removeAllComponents()
                        val tests = testDataProvider.items.map { it.test }.toSet()
                        if (tests.isNotEmpty()) {
                            dxViewLayout.addComponent(ProgressBar().apply {
                                isIndeterminate = true
                            })
                            val ui = this.ui
                            thread(start = true) {
                                val diagnosticCodesView = call_TestDxView(tests).toDiagnosticCodesView()
                                ui.access {
                                    dxViewLayout.removeAllComponents()
                                    dxViewLayout.addComponentsAndExpand(diagnosticCodesView)
                                }
                            }
                        }
                    }
                }
            })
        }
        content = login()
    }

    private fun login(): Component {
        val session = VaadinSession.getCurrent()
        if (session.getAttribute("tps/user") != null) return contentMain
        return VerticalLayout().apply {
            setMargin(false)
            isSpacing = false
            addComponent(HorizontalLayout().apply {
                setWidth(100f, Sizeable.Unit.PERCENTAGE)
                addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR)
                addComponent(Label(appCaption).apply {
                    addStyleName(ValoTheme.LABEL_COLORED)
                    addStyleName(ValoTheme.LABEL_LARGE)
                    addStyleName(ValoTheme.LABEL_BOLD)
                })
            })
            val loginLayout = HorizontalLayout().apply {
                setMargin(true)
                val userName = TextField("User Name")
                val password = PasswordField("Password")
                val button = Button("Log In").apply {
                    addStyleName(ValoTheme.BUTTON_PRIMARY)
                    setClickShortcut(ShortcutAction.KeyCode.ENTER)
                    addClickListener {
                        if (userName.value == "Brl" && password.value == "sales") {
                            session.setAttribute("tps/user", true)
                            content = contentMain
                        } else {
                            Notification.show("Invalid User Name or Password, please try again")
                        }
                    }
                }
                addComponents(userName, password, button)
                setComponentAlignment(button, Alignment.BOTTOM_LEFT)
                userName.focus()
            }
            addComponents(loginLayout)
            setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER)
        }
    }

    @WebServlet(urlPatterns = arrayOf("/tps/*"), name = "TPS-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TestPanelSimulatorUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}

var Grid.Column<*, *>.renderer: Renderer<*>?
    get() = null
    set(value) {
        @Suppress("UNCHECKED_CAST")
        setRenderer(value as? com.vaadin.ui.renderers.Renderer<Any?>)
    }

class ResultTestPay(private val details: Details) {
    val test = details["Test"] as? String ?: ""
    val testName get() = details["TestName"] as? String ?: ""
    val medicareFee get() = details["MedicareFee"] as? Double
    val expectFee get() = details["ExpectFee"] as? Double
    val expectValue get() = details["ExpectValue"] as? Double
    val payProbability get() = details["PayProbability"] as? String

    override fun toString(): String = "$test | $testName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResultTestPay) return false
        if (test != other.test) return false
        return true
    }

    override fun hashCode(): Int = test.hashCode()

}

private val dollarFormat = DecimalFormat("$#,##0.00")
private val percentFormat = DecimalFormat("#%")

private fun Result.toSimilarPanelView() = HorizontalLayout().apply {
    setSizeUndefined()
    val scalars = scalars()
    val matchFound = scalars["MatchFound"] as? Boolean ?: false
    val details = when {
        matchFound -> gridOf("BestMatchPanelDetails")?.apply {
            setWidth(42f, Sizeable.Unit.EM)
            if (heightByRows > 8) {
                heightByRows = 8.0
            }
        }
        else -> null
    }

    if (details != null) {
        val match = scalars["Match"] as? Double
        val bestMatchPanel = scalars["BestMatchPanel"] as? String
        val bestMatchPanelName = scalars["BestMatchPanelName"] as? String
        addComponent("<strong>Similar Panel:</strong>".toBoldLabel())
        details.caption = "Most Similar Panel: $bestMatchPanel | $bestMatchPanelName"
        addComponent(PopupView("<strong>$bestMatchPanel | $bestMatchPanelName</strong>", VerticalLayout().apply {
            setWidth(100f, Sizeable.Unit.PERCENTAGE)
            setHeightUndefined()
            setMargin(true)
            addComponent(details)
        }).apply {
            setSizeUndefined()
            isHideOnMouseOut = false
        })
        addComponent("(<strong>%s</strong> similarity)"
                .format(match?.let { percentFormat.format(it) })
                .toBoldLabel())
    } else {
        addComponent(Label().apply {
            addStyleName(ValoTheme.LABEL_BOLD)
        })
    }
}

private fun Result.toDiagnosticCodesView() = VerticalLayout().apply {
    setSizeFull()
    setMargin(false)
    val scalars = scalars()
    val matchFound = scalars["MatchFound"] as? Boolean ?: false
    val details = when {
        matchFound -> gridOf("TestDxPay")?.apply {
            caption = null
            setSizeFull()
        }
        else -> null
    }

    val msg = scalars["Msg"] as? String ?: "Commonly used diagnosis codes for selected test composition:"
    addComponent(msg.toBoldLabel())

    if (details != null) {
        addComponentsAndExpand(details)
    }
}

private @Language("HTML") fun String.toBoldLabel(): Label = Label().apply {
    addStyleName(ValoTheme.LABEL_BOLD)
    contentMode = ContentMode.HTML
    value = this@toBoldLabel
}

private fun call_ResultTestPay(): Result = Result(MRSS.call("ResultTestPay", "0.5", json {}))

private fun call_SimilarPanel(tests: Set<String>): Result = Result(MRSS.call("SimilarPanel", "0.5", json {
    add("ResultTest", array { tests.forEach { add(it) } })
}))

private fun call_TestDxView(tests: Set<String>): Result = Result(MRSS.call("TestDxView", "0.5", json {
    add("ResultTest", array { tests.forEach { add(it) } })
}))
