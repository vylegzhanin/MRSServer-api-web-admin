package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.*
import com.simagis.mrss.json
import com.vaadin.annotations.Push
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.Page
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import java.util.logging.Logger
import javax.servlet.annotation.WebServlet
import kotlin.concurrent.thread

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 6/7/2017.
 */
private const val appCaption = "Test Panels View (API version $apiVersion - Prototype)"

@Title(appCaption)
@Push
class TestPanelsViewUI : UI() {
    private val log = Logger.getLogger(javaClass.name)
    private val contains: (String, String) -> Boolean = { itemCaption, filterText -> itemCaption.contains(filterText, ignoreCase = true) }

    private val splitPanel = HorizontalSplitPanel().apply {
        setSplitPosition(720f, Sizeable.Unit.PIXELS)
    }

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
            addComponentsAndExpand(splitPanel)
        }

        thread(start = true) {
            val grid = call_ListPanels().gridOf("TestPanels",
                    setupUI = { setSizeFull() },
                    setupColumns = {
                        setupColumnsDefault(it)
                        columns.firstOrNull { it.caption == "Pos" }?.isHidden = true
                    }
            )?.apply {
                addStyleName("smallgrid")
                addSelectionListener {
                    val pos = it.firstSelectedItem.orElse(null)?.get("Pos") as? Number
                    if (pos == null) splitPanel.secondComponent = null else {
                        thread(start = true) {
                            val details = VerticalLayout().apply {
                                val panelDetails = call_PanelDetails(pos.toInt())
                                addComponent(HorizontalLayout().apply {
                                    addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
                                    setMargin(false)
                                    fun ScalarEntry.asSimpleField() = TextField(key, value.toString()).apply {
                                        isReadOnly = true
                                    }
                                    panelDetails.scalars().forEach { entry ->
                                        addComponent(VerticalLayout().apply {
                                            setMargin(false)
                                            addComponent(entry.asSimpleField())
                                        })
                                    }
                                })
                                addComponent(panelDetails.gridOf("CptDetails"))
                                addComponent(panelDetails.gridOf("CommonDx"))
                            }
                            access {
                                splitPanel.secondComponent = details
                            }
                        }
                    }
                }
            }
            access {
                splitPanel.firstComponent = grid
            }
        }
    }

    @WebServlet(urlPatterns = arrayOf("/tpv/*"), name = "TPV-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TestPanelsViewUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}

private fun call_ListPanels(): Result = Result(MRSS.call("ListPanels", apiVersion, json {}))
private fun call_PanelDetails(pos: Int): Result = Result(MRSS.call("PanelDetails", apiVersion, json { add("Pos", pos) }))


