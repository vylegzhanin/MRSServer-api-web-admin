package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.CallException
import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.*
import com.simagis.mrss.json
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.ExternalResource
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
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
    private val apiVersion = "0.2"
    private val contains: (String, String) -> Boolean = { itemCaption, filterText -> itemCaption.contains(filterText, ignoreCase = true) }

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

    private val filingCodeF = ComboBox<FilingCode>("Insurance Plan Type").apply {
        setItemCaptionGenerator { "${it.code}: ${it.description}" }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
    }

    private val dxF = TextField("Diagnosis Code (ICD-10)", "Z0000")

    private val genderF = RadioButtonGroup<Gender>("Patient Gender", Gender.values().toList()).apply {
        setItemCaptionGenerator { it.name }
        setSelectedItem(Gender.M)
        addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL)
    }

    private val ageF = TextField("Patient Age", "60")

    private val predictBtn: Button = Button("Predict Payment").apply {
        addStyleName(ValoTheme.BUTTON_PRIMARY)
        setClickShortcut(ShortcutAction.KeyCode.ENTER)
        addClickListener {
            try {
                val result = call_PredictTestPay()
                splitPanel.secondComponent = result.asComponent()
            } catch(e: Throwable) {
                showError(e, "Prediction Error: %s")
            }
        }
    }

    private val splitPanel = HorizontalSplitPanel().apply {
        splitPosition = 35f
        firstComponent = VerticalLayout(testF, payerF, filingCodeF, dxF, ageF, genderF, predictBtn)
    }

    override fun init(request: VaadinRequest) {
        content = splitPanel

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

    private @Volatile var testMap: Map<String, Test> = emptyMap()

    private fun call_ListTestNames(): List<Test> = (MRSS.call("ListTestNames", apiVersion)["TestList"] as? JsonObject)
            ?.toItemList("Test", "TestName") {
                Test(it.str(0), it.str(1))
            }
            ?.also { list ->
                testMap = mutableMapOf<String, Test>().apply {
                    list.forEach { this[it.id] = it }
                }
            } ?: throw CallException("TestList not found")

    private fun call_FilingCodes(): List<FilingCode> = (MRSS.call("FilingCodes", apiVersion)["FilingCodes"] as? JsonObject)
            ?.toItemList("Code", "Description") {
                FilingCode(it.str(0), it.str(1))
            }
            ?: throw CallException("TestList not found")

    private fun Test.call_ListPayers(): List<Payer> = MRSS.call("ListPayers", apiVersion,
            json { add("Test", id) })
            .toItemList("Payers") {
                Payer(it.str(0))
            }

    private fun call_PredictTestPay(): Result = Result(MRSS.call("PredictTestPay", apiVersion, json {
        add("in_prn", payerF.value.name)
        add("in_test", testF.value.id)
        add("in_dx", dxF.value)
        add("in_ptnG", genderF.value.name)
        add("in_ptnAge", ageF.value.toInt())
        add("in_fCode", filingCodeF.value.code)
    }))

    private fun Result.toGrid() = Grid<Details>("Details").also { grid ->
        grid.setWidth(100f, Sizeable.Unit.PERCENTAGE)
        val details = details()
        grid.setItems(details)
        grid.heightByRows = details.size.toDouble()
        detailsKeys().forEach { key ->
            grid.addColumn({ details: Details -> details[key] }).apply {
                caption = key
            }
        }
    }

    private fun Result.asComponent() = VerticalLayout().apply {
        addComponent(HorizontalLayout().apply {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            setMargin(false)
            scalars().forEach { entry ->
                addComponent(VerticalLayout().apply {
                    setMargin(false)
                    addComponent(when (entry.key) {
                        "NeedABN" -> entry.asNeedABN(this@asComponent)
                        else -> entry.asSimpleField()
                    })
                })
            }
        })
        addComponent(toGrid())
    }

    private fun ScalarEntry.asSimpleField() = TextField(when (key) {
        "ExpectFee" -> "Expected Test Fee"
        "StatValue" -> "Predicted Test Value"
        else -> key
    }, value.toString()).apply { isReadOnly = true }

    private fun ScalarEntry.asNeedABN(result: Result) = HorizontalLayout().apply {
        caption = "ABN Form Needed?"
        val value = this@asNeedABN.value
        val item = when (value) {
            is Number -> if (value.toDouble() > 0.9) "Yes" else "No"
            else -> "Undefined"
        }
        addComponent(RadioButtonGroup<String>().apply {
            setItems(item)
            setItemCaptionGenerator { it }
            setSelectedItem(item)
            addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL)
        })
        if (item == "Yes") {
            val link = Link().apply {
                isCaptionAsHtml = true
                caption = VaadinIcons.EXTERNAL_LINK.html + " Open ABN Form"
                targetName = "_blank"
                resource = ExternalResource(VaadinServlet.getCurrent().servletContext
                        .contextPath + "/abn?id=${result.registerABNSessionId()}")
            }
            addComponent(link)
            setComponentAlignment(link, Alignment.MIDDLE_LEFT)
        }
    }

    private fun Result.registerABNSessionId(): String {
        val uuid = UUID.randomUUID().toString()
        val scalars = scalars()
        fun esc(vararg scalar: Any?): String {
            return scalar
                    .filter { it != null }
                    .joinToString(separator = "")
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
        }

        val test = scalars["Test"]?.let { testMap[it] }
        val reasonText = details().map { esc(it["Reason"]) }.joinToString(separator = ", ")
        ABNs[uuid] = ABN(
                testText = esc(test?.id, ", ", test?.name),
                testExpectFee = esc("$", scalars["ExpectFee"]),
                reasonText = reasonText
        )
        return uuid
    }

    @WebServlet(urlPatterns = arrayOf("/ptp/*", "/VAADIN/*"), name = "PTP-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PredictTestPayUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}

