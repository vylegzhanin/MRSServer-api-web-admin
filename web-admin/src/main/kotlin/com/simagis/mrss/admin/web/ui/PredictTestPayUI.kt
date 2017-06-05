package com.simagis.mrss.admin.web.ui

import com.simagis.mrss.CallException
import com.simagis.mrss.MRSS
import com.simagis.mrss.admin.web.ui.ptp.*
import com.simagis.mrss.json
import com.simagis.mrss.set
import com.vaadin.annotations.Title
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.event.ShortcutAction
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.*
import com.vaadin.ui.*
import com.vaadin.ui.renderers.HtmlRenderer
import com.vaadin.ui.renderers.Renderer
import com.vaadin.ui.renderers.TextRenderer
import com.vaadin.ui.themes.ValoTheme
import okhttp3.HttpUrl
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.json.JsonObject
import javax.json.JsonString
import javax.servlet.annotation.WebServlet

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */
const val appCaption = "PayPredict API Demo App (API version $apiVersion - Prototype)"

@Title(appCaption)
class PredictTestPayUI : UI() {
    private val log = Logger.getLogger(javaClass.name)
    private val contains: (String, String) -> Boolean = { itemCaption, filterText -> itemCaption.contains(filterText, ignoreCase = true) }

    private val testF: ComboBox<Test?> = ComboBox<Test?>("Test Name").apply {
        setItemCaptionGenerator { it?.let { "${it.id}: ${it.name}" } }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        isEnabled = false
        addSelectionListener {
            val test: Test? = it.selectedItem.orElse(null)
            payerF.clear()
            payerF.isEnabled = test != null
            payerF.setItems(contains, test?.call_ListPayers() ?: emptyList())
            payerF.focus()

            filingCodeF.clear()
            filingCodeF.isEnabled = false
            updatePredictBtn()
        }
    }

    private val payerF: ComboBox<Payer?> = ComboBox<Payer?>("Payer").apply {
        setItemCaptionGenerator { it?.name }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        isEnabled = false
        addSelectionListener {
            val payer: Payer? = it.selectedItem.orElse(null)
            filingCodeF.clear()
            filingCodeF.isEnabled = payer != null

            dxF.clear()
            dxF.isEnabled = payer != null

            val test = testF.value
            if (payer != null && test != null) {
                val selected = test to payer
                filingCodeF.setItems(contains, selected.call_FilingCodes())
                filingCodeF.focus()
                dxF.setItems(selected.call_DxCodes())
            }
            updatePredictBtn()
        }
    }

    private val filingCodeF: ComboBox<FilingCode?> = ComboBox<FilingCode?>("Insurance Plan Type").apply {
        setItemCaptionGenerator { it?.let { "${it.code}: ${it.description}" } }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        isEnabled = false
        addSelectionListener {
            updatePredictBtn()
        }
    }

    private val dxF: ComboBox<DxCode?> = ComboBox<DxCode?>("Diagnosis Code (ICD-10)").apply {
        setItemCaptionGenerator { it?.let { "${it.code}: ${it.description}" } }
        setWidth(100f, Sizeable.Unit.PERCENTAGE)
        isEnabled = false
        addSelectionListener {
            updatePredictBtn()
        }
    }

    private val genderF = RadioButtonGroup<Gender?>("Patient Gender", Gender.values().toList()).apply {
        setItemCaptionGenerator { it?.name }
        setSelectedItem(Gender.M)
        addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL)
    }

    private val ageF = TextField("Patient Age", "60")

    private val predictBtn: Button = Button("Predict Payment").apply {
        addStyleName(ValoTheme.BUTTON_PRIMARY)
        isEnabled = false
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

    private fun updatePredictBtn() {
        predictBtn.isEnabled = listOf(
                dxF.value,
                filingCodeF.value,
                payerF.value,
                testF.value
        ).all { it != null }
    }

    private val splitPanel = HorizontalSplitPanel().apply {
        setSplitPosition(500f, Sizeable.Unit.PIXELS)
        firstComponent = VerticalLayout(testF, payerF, filingCodeF, dxF, ageF, genderF, predictBtn)
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

        try {
            testF.setItems(contains, call_ListTestNames())
            testF.isEnabled = true
            testF.focus()
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
            } ?: throw CallException("TestList not found in response")

    private fun Pair<Test, Payer>.call_FilingCodes(): List<FilingCode> = (MRSS.call("FilingCodes", apiVersion,
            json {
                this["Test"] = first.id
                this["Payer"] = second.name
            })["FilingCodes"] as? JsonObject)
            ?.toItemList("Code", "Description") {
                FilingCode(it.str(0), it.str(1))
            }
            ?: throw CallException("FilingCodes not found in response")

    private fun Pair<Test, Payer>.call_DxCodes(): List<DxCode> = (MRSS.call("GetDxCodes", apiVersion,
            json {
                this["inTest"] = first.id
                this["inPayer"] = second.name
            })["DxCodes"] as? JsonObject)
            ?.toItemList("dx1", "description") {
                DxCode(it.str(0), it.str(1))
            }
            ?: throw CallException("DxCodes not found in response")

    private fun Test.call_ListPayers(): List<Payer> = MRSS.call("ListPayers", apiVersion,
            json { add("Test", id) })
            .toItemList("Payers") {
                Payer(it.str(0))
            }

    private fun call_PredictTestPay(): Result = Result(MRSS.call("PredictTestPay", apiVersion, json {
        this["in_prn"] = payerF.value?.name
        this["in_test"] = testF.value?.id
        this["in_dx"] = dxF.value?.code
        this["in_ptnG"] = genderF.value?.name
        this["in_ptnAge"] = ageF.value.toInt()
        this["in_fCode"] = filingCodeF.value?.code
    }))

    private fun Result.gridOf(
            name: String,
            gridCaption: String = name.capitalize(),
            itemsFilter: (List<Details>) -> List<Details> = { it },
            setupColumns: Grid<Details>.(List<String>) -> Unit = { setupColumnsDefault(it) }): Grid<Details>? {
        val items: List<Details> = itemsFilter(asList(name))
        return when {
            items.isNotEmpty() -> Grid<Details>(gridCaption).also { grid ->
                grid.setWidth(100f, Sizeable.Unit.PERCENTAGE)
                grid.setItems(items)
                grid.heightByRows = items.size.toDouble()
                val list = keysOf(name)
                setupColumns(grid, list)
            }
            else -> null
        }
    }

    private fun Grid<Details>.setupColumnsDefault(keys: List<String>) = keys.forEach { key ->
        addColumn({ details: Details -> details[key] }).apply {
            caption = key
        }
    }

    private fun Result.asComponent() = VerticalLayout().apply {
        addComponent(HorizontalLayout().apply {
            addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
            setMargin(false)
            scalars().forEach { entry ->
                addComponent(VerticalLayout().apply {
                    setMargin(false)
                    when (entry.key) {
                        "NeedABN" -> addComponent(entry.asNeedABN(this@asComponent))
                        "NeedPrecert" -> addComponent(entry.asNeedPrecert(this@asComponent))
                        "RunTimeSec" -> {
                            Notification(
                                    "RunTimeSec: %.3f".format(entry.value),
                                    Notification.Type.TRAY_NOTIFICATION)
                                    .show(Page.getCurrent())
                        }
                        else -> addComponent(entry.asSimpleField())
                    }
                })
            }
        })
        gridOf("details", "Risk Profile") { keys ->
            fun map(details: Details, key: String): Any? {
                return when (key) {
                    "Code" -> {
                        val code = details[key]
                        val msg = details["Msg"] as? String
                        if (msg.isNullOrBlank())
                            code else
                            """<strong title="${msg.esc()}">$code<sup style="color:red">*<sup></strong>"""
                    }
                    "MCFee",
                    "ExpFee" -> (details[key] as? Number).dollars()
                    else -> details[key]
                }
            }
            keys.forEach { key ->
                addColumn({ details: Details -> map(details, key) }).apply {
                    caption = key
                    isHidden = key == "Msg"
                    when (key) {
                        "Code" -> {
                            caption = "Warning"
                            setRenderer(htmlRenderer())
                        }
                        "MCFee",
                        "ExpFee" -> setRenderer(TextRenderer())
                    }
                }
            }
        }?.let { addComponent(it) }
        gridOf("DenialCodeDescription", "Adjustment Reasons") { keys ->
            setupColumnsDefault(keys)
            columns.forEach {
                when (it.caption) {
                    "Reason" -> it.width = 96.toDouble()
                }
            }
        }?.let { addComponent(it) }
        gridOf("details", "Warnings", itemsFilter = {
            it.filter { !(it["Msg"] as? String).isNullOrBlank() }.distinctBy { it["Msg"] }
        }) { keys ->
            val warningKeys = setOf("Code", "Msg")
            setupColumnsDefault(keys.filter { it in warningKeys })
            columns.forEach {
                when (it.caption) {
                    "Code" -> it.width = 96.toDouble()
                    "Msg" -> it.caption = "Description"
                }
            }
        }?.let { addComponent(it) }

        // http://nj1etlpiped01:8080/query/ppc?cpt=&dx1=&fCode=&prn=
        HttpUrl.parse("http://nj1etlpiped01:8080/query/ppc")?.newBuilder()?.let { url ->
            val cpt = asList("details")
                    .map { it["Cpt"] as? String }
                    .filter { it != null }
                    .distinct()
                    .joinToString(separator = ",")
            url.addQueryParameter("prn", payerF.value?.name ?: "")
            url.addQueryParameter("fCode", filingCodeF.value?.code ?: "")
            url.addQueryParameter("dx", dxF.value?.code)
            url.addQueryParameter("cpt", cpt)
            url.build().url()
        }?.let { url ->
            val link = Link().apply {
                isCaptionAsHtml = true
                caption = "Open Claims".withExternalLinkIcon()
                targetName = "_blank"
                resource = ExternalResource(url)
            }
            addComponent(link)
            setComponentAlignment(link, Alignment.MIDDLE_RIGHT)
        }

        json.getJsonArray("CommonDx")?.let { dxArray ->
            if (dxArray.isNotEmpty()) {
                addComponent(HorizontalLayout().apply {
                    caption = "Common Diagnostic Codes (ICD-10)"
                    addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING)
                    addStyleName(ValoTheme.LAYOUT_CARD)
                    setMargin(false)
                    isSpacing = false
                    dxArray.forEach { dx ->
                        if (dx is JsonString) {
                            val dxCode = dx.string
                            addComponent(Button(dxCode).apply {
                                addStyleName(ValoTheme.BUTTON_LINK)
                                addStyleName(ValoTheme.BUTTON_SMALL)
                                addClickListener {
                                    dxF.value = (dxF.dataProvider as? ListDataProvider<DxCode?>)
                                            ?.items
                                            ?.firstOrNull { it?.code == dxCode }
                                }
                            })
                        }
                    }
                })
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun htmlRenderer() = HtmlRenderer() as? Renderer<Any?>
            ?: throw AssertionError("invalid vaadin HtmlRenderer")

    private fun String.withExternalLinkIcon(): String {
        val separator = """<span style="text-decoration: none; display: inline-block; width: 3pt"></span>"""
        return "$this$separator${VaadinIcons.EXTERNAL_LINK.html}"
    }

    private fun ScalarEntry.asSimpleField() = TextField(when (key) {
        "ExpectFee" -> "Expected Test Fee"
        "StatValue" -> "Predicted Test Value"
        else -> key
    }, when(key) {
        "ExpectFee" -> (value as? Number).dollars()
        else -> value.toString()
    }).apply { isReadOnly = true }

    private fun ScalarEntry.asNeedABN(result: Result) = asLinkYesNo(
            "ABN form needed",
            value as? Boolean,
            "Open ABN Form",
            { "/abn?id=${result.registerABN()}" }
    )

    private fun ScalarEntry.asNeedPrecert(result: Result) = asLinkYesNo(
            "Precertification needed",
            value as? Boolean,
            "Open Precertification Form",
            { "/dtpc?id=${result.registerDTPC()}" }
    )

    private fun asLinkYesNo(topCaption: String, value: Boolean?, linkCaption: String, href: () -> String) = HorizontalLayout().apply {
        caption = topCaption
        val item = when (value) {
            true -> "Yes"
            false -> "No"
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
                caption = linkCaption.withExternalLinkIcon()
                targetName = "_blank"
                resource = ExternalResource(VaadinServlet.getCurrent().servletContext.contextPath + href())
            }
            addComponent(link)
            setComponentAlignment(link, Alignment.MIDDLE_LEFT)
        }
    }

    private fun Result.registerABN(): String {
        val uuid = UUID.randomUUID().toString()
        val scalars = scalars()
        val test = scalars["Test"]?.let { testMap[it] }
        val reasons = asList("DenialCodeDescription").map { esc(it["Reason"], ": ", it["Description"]) }
        val reasonText = when {
            reasons.isNotEmpty() -> reasons
            else -> asList("details").mapNotNull { it["Msg"] as? String }.distinct().map { esc(it) }
        }.joinToString(separator = "<hr>\n")

        ABNs[uuid] = ABN(
                testText = esc(test?.id, ", ", test?.name),
                testExpectFee = esc("$", scalars["ExpectFee"]),
                reasonText = reasonText
        )
        return uuid
    }

    private fun Result.registerDTPC(): String {
        val uuid = UUID.randomUUID().toString()
        val scalars = scalars()
        fun text(name: String) = (scalars[name] as? String) ?: ""
        DTPCs[uuid] = DTPC(
                payerText = text("Payer"),
                patientAgeText = text("PatientAge"),
                patientGenderText = text("PatientGender"),
                dxText = text("DX"),
                cptCodes = asList("details").mapNotNull {
                    val code = it["Cpt"] as? String
                    val description = it["CptDescription"] as? String
                    when {
                        code != null && description != null -> CPT(code, description)
                        code != null -> CPT(code, "")
                        else -> null
                    }
                }
        )
        return uuid
    }

    @WebServlet(urlPatterns = arrayOf("/ptp/*", "/VAADIN/*"), name = "PTP-UI-Servlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PredictTestPayUI::class, productionMode = false)
    class UIServlet : VaadinServlet()
}
