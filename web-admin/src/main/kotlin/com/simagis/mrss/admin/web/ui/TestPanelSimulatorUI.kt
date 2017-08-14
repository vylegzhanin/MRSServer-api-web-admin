package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.Details
import com.simagis.mrss.admin.web.ui.ptp.Result
import com.simagis.mrss.admin.web.ui.ptp.gridOf
import com.simagis.mrss.array
import com.simagis.mrss.json
import com.vaadin.annotations.Push
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.shared.ui.ContentMode
import com.vaadin.ui.*
import com.vaadin.ui.renderers.NumberRenderer
import com.vaadin.ui.renderers.Renderer
import com.vaadin.ui.themes.ValoTheme
import java.text.DecimalFormat
import javax.servlet.annotation.WebServlet
import kotlin.concurrent.thread


/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 8/10/2017.
 */
private const val appCaption = "Test Panel Simulator (API version 0.5 - Prototype)"

@Title(appCaption)
@Theme("ipad-valo")
@Push
class TestPanelSimulatorUI : UI() {
    override fun init(request: VaadinRequest) {
        content = VerticalLayout().apply {
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
                addTab(VerticalLayout().apply {
                    caption = "Test View"
                    val data = ListDataProvider<ResultTestPay>(mutableSetOf())
                    val totals = Label("", ContentMode.HTML).apply {
                        setWidth(100f, Sizeable.Unit.PERCENTAGE)
                        addStyleName(ValoTheme.LABEL_BOLD)
                    }
                    fun updateTotals() {
                        //language=HTML
                        totals.value = """
                            <strong>Totals</strong>: Payment Probability - <strong>%s</strong>,
                                    Expect Fee - <strong>%s</strong>,
                                    Expected Value - <strong>%s</strong>"""
                                .format(
                                        percentFormat.format(data.items.minBy { it.payProbability ?: 0.0 }?.payProbability ?: 0.0),
                                        dollarFormat.format(data.items.sumByDouble { it.expectFee ?: 0.0 }),
                                        dollarFormat.format(data.items.sumByDouble { it.expectValue ?: 0.0 })
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
                        dataProvider = data
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
                            renderer = NumberRenderer(percentFormat)
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
                            val tests = data.items.map { it.test }.toSet()
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
                                    if (data.items.add(it)) data.refreshAll()
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
                                data.items.removeAll(grid.selectedItems)
                                data.refreshAll()
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
                addTab(VerticalLayout(), "CPT View").isEnabled = false
                addTab(VerticalLayout(), "Dx View").isEnabled = false
            })
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
    val payProbability get() = details["PayProbability"] as? Double

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
    val details = gridOf("BestMatchPanelDetails")
            ?.apply { setSizeUndefined() }

    if (details != null) {
        val scalars = scalars()
        addComponent(Label().apply {
            addStyleName(ValoTheme.LABEL_BOLD)
            contentMode = ContentMode.HTML
            //language=HTML
            value = "<strong>Panel Similarity: %s</strong>, Most Similar Panel:".format(
                    (scalars["Match"] as? Double)?.let { percentFormat.format(it) }
            )
        })
        val bestMatchPanel = scalars["BestMatchPanel"] as? String
        val bestMatchPanelName = scalars["BestMatchPanelName"] as? String
        details.caption = "Most Similar Panel: $bestMatchPanel | $bestMatchPanelName"
        addComponent(PopupView("<strong>$bestMatchPanel | $bestMatchPanelName</strong>", VerticalLayout().apply {
            setSizeUndefined()
            setMargin(true)
            addComponent(details)
        }).apply {
            setSizeUndefined()
            isHideOnMouseOut = false
        })
    } else {
        addComponent(Label("Similar panel not found"))
    }
}

private fun call_ResultTestPay(): Result = Result(MRSS.call("ResultTestPay", "0.5", json {}))

private fun call_SimilarPanel(tests: Set<String>): Result = Result(MRSS.call("SimilarPanel", "0.5", json {
    add("ResultTest", array { tests.forEach { add(it) } })
}))
