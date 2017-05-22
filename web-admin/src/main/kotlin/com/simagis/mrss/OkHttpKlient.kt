package com.simagis.mrss

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.StringWriter
import javax.json.Json
import javax.json.JsonArray
import javax.json.JsonObject
import javax.json.JsonObjectBuilder
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

fun String.toJsonObject(): JsonObject = Json.createReader(reader()).readObject()
fun String.toJsonArray(): JsonArray = Json.createReader(reader()).readArray()

private val PP by lazy { Json.createWriterFactory(mapOf<String, Any>(JsonGenerator.PRETTY_PRINTING to true)) }
fun JsonObject.ppString(): String = StringWriter().use { PP.createWriter(it).write(this); it.toString() }

fun JsonObject.toJsonRequest(): RequestBody = RequestBody.create(JSON, this.toString())