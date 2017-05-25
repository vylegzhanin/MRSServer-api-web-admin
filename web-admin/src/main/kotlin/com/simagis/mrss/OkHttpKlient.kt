package com.simagis.mrss

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import java.io.StringWriter
import java.math.BigDecimal
import java.math.BigInteger
import javax.json.*
import javax.json.stream.JsonGenerator

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/22/2017.
 */

val JSON by lazy { MediaType.parse("application/json; charset=utf-8") }

fun json(builder: JsonObjectBuilder.() -> Unit): JsonObject = Json
        .createObjectBuilder()
        .apply(builder)
        .build()

operator fun JsonObjectBuilder.set(key: String, value: Any?) {
    when (value) {
        null -> add(key, JsonValue.NULL)
        is Boolean -> add(key, value)
        is String -> add(key, value)
        is Int -> add(key, value)
        is Long -> add(key, value)
        is Double -> add(key, value)
        is JsonValue -> add(key, value)
        is BigDecimal -> add(key, value)
        is BigInteger -> add(key, value)
        is JsonObjectBuilder -> add(key, value)
        is JsonArrayBuilder -> add(key, value)
    }
}

fun array(builder: JsonArrayBuilder.() -> Unit): JsonArray = Json
        .createArrayBuilder()
        .apply(builder)
        .build()

fun String.toJsonObject(): JsonObject = Json.createReader(reader()).readObject()
fun String.toJsonArray(): JsonArray = Json.createReader(reader()).readArray()

private val PP by lazy { Json.createWriterFactory(mapOf<String, Any>(JsonGenerator.PRETTY_PRINTING to true)) }
fun JsonObject.ppString(): String = StringWriter().use { PP.createWriter(it).write(this); it.toString() }
fun JsonArray.ppString(): String = StringWriter().use { PP.createWriter(it).write(this); it.toString() }

fun JsonObject.toJsonRequest(): RequestBody = RequestBody.create(JSON, this.toString())
fun Response.toJsonObject(): JsonObject = body()?.string()?.toJsonObject() ?: json { }
fun Response.toJsonArray(): JsonArray = body()?.string()?.toJsonArray() ?: array { }

class CallException: RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace)
}