package com.simagis.mrss

import io.swagger.client.ApiClient
import io.swagger.client.Configuration
import io.swagger.client.api.AuthenticationAPIsApi
import io.swagger.client.api.ServicesManagementAPIsApi
import io.swagger.client.model.LoginRequest
import java.io.File
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
    private val basePath: String get() = configuration.getProperty("basePath", "http://localhost:12800")
    private val userName: String get() = configuration.getProperty("userName", "admin")
    private val password: String get() = configuration.getProperty("password", "admin")

    private val accessTokenLock: Lock = ReentrantLock()
    private var accessToken_: String? = null
    private var accessTokenExpiresOnMs_: Long = 0
    private fun login(): ApiClient?  = accessTokenLock.withLock {
        if (System.currentTimeMillis() > accessTokenExpiresOnMs_ || accessToken_ == null) {
            val apiClient = Configuration.getDefaultApiClient()
            apiClient.basePath = basePath
            val loginRequest = LoginRequest()
                    .username(userName)
                    .password(password)
            val response = AuthenticationAPIsApi().login(loginRequest)
            accessToken_ = response.accessToken
            accessTokenExpiresOnMs_ = now().plus(5, ChronoUnit.MINUTES).toEpochMilli()
            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken_)
            apiClient
        } else
            null
    }

    val apiClient: ApiClient get() = login() ?: Configuration.getDefaultApiClient()

    val servicesManagementAPIsApi: ServicesManagementAPIsApi get() {
        login()
        return ServicesManagementAPIsApi()
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