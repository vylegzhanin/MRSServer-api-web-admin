package com.simagis.mrss

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import java.io.StringWriter
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