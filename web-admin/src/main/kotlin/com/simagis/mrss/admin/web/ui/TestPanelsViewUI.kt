package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.*
import com.simagis.mrss.json
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.server.SerializablePredicate
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import com.vaadin.ui.renderers.HtmlRenderer
import com.vaadin.ui.themes.ValoTheme
import java.util.*
import javax.servlet.annotation.WebServlet


/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 6/7/2017.
 */
private const val appCaption = "Test Panels View (API version 0.5 - Prototype)"

@Title(appCaption)
@Theme("facebook-valo")
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
                            addColumn {
                                val iPos = (it["Pos"] as? Number)?.toInt()
                                val test = it["Test"]
                                """<a href="/tpv/?pos=$iPos" target="_blank">$test</a>"""
                            }.apply {
                                caption = "Test"
                                width = 100.0
                                id = "Test+Pos"
                                setComparator { o1, o2 ->
                                    Objects.compare(o1, o2, compareBy<Details?> { it?.get("Test") as? String })
                                }
                                @Suppress("UNCHECKED_CAST")
                                setRenderer(HtmlRenderer() as? com.vaadin.ui.renderers.Renderer<Any?>)
                            }
                            setupColumnsDefault(it)
                            val filtersRow = appendHeaderRow()
                            val filters = mutableMapOf<String, TextField>()
                            columns.forEach { column: Grid.Column<Details, *> ->
                                when (column.id) {
                                    "Test", "Pos" -> column.isHidden = true
                                    "TestName" -> column.width = 240.0
                                    "Count", "ExpectFee", "PredictFee" -> column.width = 120.0
                                    "CptList", "Top10DX" -> column.width = 280.0
                                    "NCPTs", "MCFee", "Sd" -> column.isHidden = true
                                }
                                if (!column.isHidden) {
                                    filtersRow.getCell(column)?.component = TextField().apply {
                                        setWidth(100f, Sizeable.Unit.PERCENTAGE)
                                        addStyleName(ValoTheme.TEXTFIELD_TINY)
                                        placeholder = "Filter"
                                        addValueChangeListener {
                                            (dataProvider as? ListDataProvider)?.setFilter(filters.toFilter())
                                        }
                                        filters[column.caption] = this
                                    }
                                }
                            }
                        })
                        ?.apply {
                            frozenColumnCount = columns.indexOfFirst { it.id == "TestName"} + 1
                        })
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

    private fun Map<String, TextField>.toFilter(): SerializablePredicate<Details> = SerializablePredicate { details: Details ->
        for ((id, field) in this) {
            val filter = field.value
            if (!filter.isNullOrBlank()) {
                val value = details[id]
                if (value is String && !value.contains(filter, ignoreCase = true)) return@SerializablePredicate false
                else
                    if (value is Number) {
                        if (filter.startsWith(">")) {
                            val asNumber = filter.removePrefix(">").toDoubleOrNull()
                            if (asNumber != null) {
                                if (value.toDouble() < asNumber) return@SerializablePredicate false
                                continue
                            }
                        }
                        if (filter.startsWith("<")) {
                            val asNumber = filter.removePrefix("<").toDoubleOrNull()
                            if (asNumber != null) {
                                if (value.toDouble() > asNumber) return@SerializablePredicate false
                                continue
                            }
                        }
                        if (!value.toString().contains(filter, ignoreCase = true)) return@SerializablePredicate false
                    }
            }
        }
        true
    }

    @WebServlet(urlPatterns = arrayOf("/tpv/*"), name = "TPV-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TestPanelsViewUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}

private fun call_ListPanels(): Result = Result(MRSS.call("ListPanels", "0.5", json {}))
private fun call_PanelDetails(pos: Int): Result = Result(MRSS.call("PanelDetails", "0.5", json { add("Pos", pos) }))
