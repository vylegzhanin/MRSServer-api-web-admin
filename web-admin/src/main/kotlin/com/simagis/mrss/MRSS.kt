package com.simagis.mrss

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.time.Instant.now
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/21/2017.
 */
object MRSS {
    fun newRequest(path: String, builder: Request.Builder.() -> Unit = {}): Call = okHttpClient.newCall(Request.Builder()
            .addHeader("Authorization", "Bearer ${accessToken()}")
            .url("$basePath$path")
            .apply(builder)
            .build())

    private val okHttpClient = OkHttpClient()
    private val basePath: String get() = configuration.getProperty("basePath", "http://localhost:12800")
    private val userName: String get() = configuration.getProperty("userName", "admin")
    private val password: String get() = configuration.getProperty("password", "admin")

    private val accessTokenLock: Lock = ReentrantLock()
    private var accessToken_: String? = null
    private var accessTokenExpiresOnMs_: Long = 0
    private fun accessToken(): String = accessTokenLock.withLock {
        if (System.currentTimeMillis() > accessTokenExpiresOnMs_ || accessToken_ == null) {
            val request = Request.Builder()
                    .url("$basePath/login")
                    .method("POST", json {
                        add("username", userName)
                        add("password", password)
                    }.toJsonRequest())
                    .build()
            okHttpClient.newCall(request).execute().use { response ->
                when {
                    response.code() == 200 -> response.body()?.string()?.toJsonObject()?.run {
                        accessToken_ = getString("access_token")
                        val expiresIn = getJsonNumber("expires_in").longValue()
                        accessTokenExpiresOnMs_ = now().plus(expiresIn - 20, ChronoUnit.SECONDS).toEpochMilli()
                    }
                    else -> throw IOException(response.message())
                }
            }
        }
        accessToken_ ?: throw IOException("invalid access_token")
    }

    private val configuration by lazy {
        Properties().apply {
            val dir = File("/MRSS").apply { if (!exists()) mkdir() }
            val file = File(dir, "configuration.properties").apply {
                if (!exists())
                    outputStream().use {
                        MRSS.javaClass.getResourceAsStream("configuration.properties").copyTo(it)
                    }
            }
            file.inputStream().use { load(it) }
        }
    }
}