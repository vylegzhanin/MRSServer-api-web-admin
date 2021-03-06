package com.simagis.mrss.admin.web.ui.ptp

import com.vaadin.server.Sizeable
import com.vaadin.ui.Grid
import javax.json.*

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */

const val apiVersion = "0.4"

data class Test(val id: String, val name: String)
data class Payer(val name: String)
data class FilingCode(val code: String, val description: String)
data class DxCode(val code: String, val description: String)
enum class Gender {M, F, }

class Result(val json: JsonObject) {

    fun scalars(): Map<String, Any> = mutableMapOf<String, Any>().apply {
        json.keys.forEach { key ->
            json[key].let { if (it.isScalar) this[key] = it.scalar() }
        }
    }

    fun keysOf(name: String): List<String> = (json[name] as? JsonObject)?.keys?.toList() ?: emptyList()

    fun asList(name: String): List<Details> = (json[name] as? JsonObject)?.let { detailsJson ->
        val keyNames: Array<String> = detailsJson.keys.toTypedArray()
        detailsJson.toItemList(*keyNames) {
            mutableMapOf<String, Any>().apply {
                keyNames.forEachIndexed { index, name1 ->
                    this[name1] = it.scalar(index)
                }
            }
        }
    } ?: emptyList()
}

typealias Details = Map<String, Any>

typealias ScalarEntry = Map.Entry<String, Any>

fun <T> JsonObject.toItemList(vararg keyNames: String, builder: (List<JsonValue?>) -> T): List<T> {
    val arrays: Map<String, JsonArray> = mutableMapOf<String, JsonArray>().apply {
        keyNames.forEach { this[it] = getJsonArray(it) }
    }
    val size = arrays.values.firstOrNull()?.size ?: 0
    return object : AbstractList<T>() {
        override val size = size
        override fun get(index: Int): T = builder(keyNames.map { arrays[it]?.get(index) }.toList())
    }
}

fun List<JsonValue?>.str(i: Int, def: String = ""): String {
    val value = getOrNull(i)
    return when(value) {
        is JsonString -> value.string
        null -> def
        else -> value.toString()
    }
}

fun List<JsonValue?>.scalar(i: Int, def: Any = ""): Any = getOrNull(i).scalar(def)

fun JsonValue?.scalar(def: Any = ""): Any = if (this == null) def else when {
    this is JsonString -> string
    this is JsonNumber -> if (isIntegral) longValue() else doubleValue()
    valueType == JsonValue.ValueType.TRUE -> true
    valueType == JsonValue.ValueType.FALSE -> false
    else -> def
}

val JsonValue?.isScalar get() = if (this == null) false else
    this is JsonString ||
    this is JsonNumber ||
    valueType == JsonValue.ValueType.TRUE ||
    valueType == JsonValue.ValueType.FALSE


fun String?.esc(): String = if (this == null)
    "" else
    replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quote;")

fun Number?.dollars(def: String = "", nil: String = ""): String = this?.let {
    "$%.2f".format(it.toDouble()).let {
        if (it != "$0.00")
            it else
            nil
    }
} ?: def


fun Result.gridOf(
        name: String,
        gridCaption: String? = name.capitalize(),
        itemsFilter: (List<Details>) -> List<Details> = { it },
        setupItems: Grid<Details>.(List<Details>) -> Unit = { setItems(it) },
        setupUI: Grid<Details>.(List<Details>) -> Unit = { setWidth(100f, Sizeable.Unit.PERCENTAGE); heightByRows = it.size.toDouble() },
        setupColumns: Grid<Details>.(List<String>) -> Unit = { setupColumnsDefault(it) }): Grid<Details>? {
    val items: List<Details> = itemsFilter(asList(name))
    return when {
        items.isNotEmpty() -> Grid<Details>(gridCaption).also { grid ->
            grid.setupItems(items)
            grid.setupUI(items)
            setupColumns(grid, keysOf(name))
        }
        else -> null
    }
}

fun Grid<Details>.setupColumnsDefault(keys: List<String>) = keys.forEach { key ->
    addColumn({ details: Details -> details[key] }).apply {
        caption = key
        id = key
    }
}