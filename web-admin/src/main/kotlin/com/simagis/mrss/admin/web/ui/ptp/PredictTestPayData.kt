package com.simagis.mrss.admin.web.ui.ptp

import javax.json.JsonArray
import javax.json.JsonObject
import javax.json.JsonString
import javax.json.JsonValue

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */
data class Test(val id: String, val name: String)
data class Payer(val name: String)
data class FilingCode(val code: String, val description: String)

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
