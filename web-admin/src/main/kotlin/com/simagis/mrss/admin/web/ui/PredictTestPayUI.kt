package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.CallException
import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.*
import com.simagis.mrss.json
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.json.JsonObject
import javax.servlet.annotation.WebServlet

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */
@Title("Predict Test Pay")
class PredictTestPayUI : UI() {
    private val log = Logger.getLogger(javaClass.name)
    private val contains: (String, String) -> Boolean = { itemCaption, filterText -> itemCaption.contains(filterText) }

    private val testF = ComboBox<Test>("Test Name").apply {
        setItemCaptionGenerator { "${it.id}: ${it.name}" }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        isEnabled = false
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
        content = HorizontalSplitPanel().apply {
            splitPosition = 25f
            firstComponent =  VerticalLayout(testF, payerF, filingCodeF)
        }

        try {
            testF.setItems(contains, call_ListTestNames())
            testF.isEnabled = true
            filingCodeF.setItems(contains, call_FilingCodes())
        } catch(e: Throwable) {
            showError(e, "Prediction Server Error: %s")
        }
    }

    private fun showError(e: Throwable, captionFormat: String = "%s") {
        val uuid = UUID.randomUUID()
        val caption = captionFormat.format(e.message)
        log.log(Level.SEVERE, "$caption $uuid", e)
        Notification.show(caption,
                """${e.javaClass.simpleName}
                            see server log for details
                            $uuid""",
                Notification.Type.ERROR_MESSAGE)
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

    @WebServlet(urlPatterns = arrayOf("/ptp/*", "/VAADIN/*"), name = "PTP-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PredictTestPayUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}
