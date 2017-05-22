package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.CallException
import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.test.*
import com.simagis.mrss.json
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.ComboBox
import com.vaadin.ui.HorizontalSplitPanel
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import javax.json.JsonObject
import javax.servlet.annotation.WebServlet

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */
@Title("Test Query")
class TestUI : UI() {

    private val contains: (String, String) -> Boolean = { itemCaption, filterText -> itemCaption.contains(filterText) }

    private val testF = ComboBox<Test>("Test Name").apply {
        setItemCaptionGenerator { "${it.id}: ${it.name}" }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        addSelectionListener {
            val test: Test? = it.selectedItem.orElse(null)
            payerF.clear()
            payerF.isEnabled = test != null
            payerF.setItems(contains, test?.call_ListPayers() ?: emptyList())
        }
    }

    private val payerF: ComboBox<Payer> = ComboBox<Payer>("Payer").apply {
        setItemCaptionGenerator { it.name }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        isEnabled = false
    }

    private val filingCodeF = ComboBox<FilingCode>("Filing Code").apply {
        setItemCaptionGenerator { "${it.code}: ${it.description}" }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
    }

    override fun init(request: VaadinRequest) {
        testF.setItems(contains, call_ListTestNames())
        filingCodeF.setItems(contains, call_FilingCodes())

        content = HorizontalSplitPanel().apply {
            firstComponent =  VerticalLayout(testF, payerF, filingCodeF)
        }
    }

    private fun call_ListTestNames(): List<Test> = (MRSS.call("ListTestNames", "0.1")["TestList"] as? JsonObject)
            ?.toItemList("Test", "TestName") {
                Test(it.str(0), it.str(1))
            }
            ?: throw CallException("TestList not found")

    private fun call_FilingCodes(): List<FilingCode> = (MRSS.call("FilingCodes", "0.1")["FilingCodes"] as? JsonObject)
            ?.toItemList("Code", "Description") {
                FilingCode(it.str(0), it.str(1))
            }
            ?: throw CallException("TestList not found")

    private fun Test.call_ListPayers(): List<Payer> = MRSS.call("ListPayers", "0.1",
            json { add("Test", id) })
            .toItemList("Payers") {
                Payer(it.str(0))
            }

    @WebServlet(urlPatterns = arrayOf("/test/*", "/VAADIN/*"), name = "TestUI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TestUI::class, productionMode = false)
    class MyUIServlet : VaadinServlet()
}

