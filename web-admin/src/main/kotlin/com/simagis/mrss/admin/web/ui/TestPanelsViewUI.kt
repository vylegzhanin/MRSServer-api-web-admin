package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.*
import com.simagis.mrss.json
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import com.vaadin.ui.renderers.HtmlRenderer
import com.vaadin.ui.themes.ValoTheme
import javax.servlet.annotation.WebServlet


/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 6/7/2017.
 */
private const val appCaption = "Test Panels View (API version $apiVersion - Prototype)"

@Title(appCaption)
class TestPanelsViewUI : UI() {
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
            val pos = request.getParameter("pos")?.toIntOrNull()
            if (pos == null) {
                addComponentsAndExpand(call_ListPanels().gridOf(
                        name = "TestPanels",
                        gridCaption = null,
                        setupUI = {
                            setSizeFull()
                            setSelectionMode(Grid.SelectionMode.NONE)
                        },
                        setupColumns = {
                            setupColumnsDefault(it)
                            columns.firstOrNull { it.caption == "Pos" }?.isHidden = true

                            addColumn({
                                (it["Pos"] as? Number)?.toInt()?.let {
                                    val icon = VaadinIcons.EXTERNAL_LINK.html
                                    """<a href="/tpv/?pos=$it" target="_blank">$icon</a>"""
                                } ?: ""
                            }).apply {
                                caption = "Details"
                                width = 80.0
                                @Suppress("UNCHECKED_CAST")
                                setRenderer(HtmlRenderer() as? com.vaadin.ui.renderers.Renderer<Any?>)
                            }
                        }))
            } else {
                addComponentsAndExpand(Panel().apply {
                    addStyleName(ValoTheme.PANEL_BORDERLESS)
                    content = VerticalLayout().apply {
                        val panelDetails = call_PanelDetails(pos)
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
                })
            }
        }
    }

    @WebServlet(urlPatterns = arrayOf("/tpv/*"), name = "TPV-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TestPanelsViewUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}

private fun call_ListPanels(): Result = Result(MRSS.call("ListPanels", apiVersion, json {}))
private fun call_PanelDetails(pos: Int): Result = Result(MRSS.call("PanelDetails", apiVersion, json { add("Pos", pos) }))


