package com.simagis.mrss.admin.web.ui.ptp

import java.time.LocalDate
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/31/2017.
 */

abstract class Registry<T> {
    private val lock = ReentrantReadWriteLock()
    private val map = mutableMapOf<String, T>()

    operator fun get(id: String?): T? = id?.let { lock.read { map[it] } }
    operator fun set(id: String, value: T): Unit = lock.write { map[id] = value }

}

object ABNs: Registry<ABN>()

data class ABN(
        val testText: String = "",
        val testExpectFee: String = "",
        val reasonText: String = ""
)

object DTPCs: Registry<DTPC>()

data class DTPC(
        val payerText: String = "",
        val dateText: String = LocalDate.now().toString(),
        val patientAgeText: String = "",
        val patientGenderText: String = "",
        val dxText: String = "",
        val cptCodes: List<CPT> = emptyList()
)

data class CPT(
        val code: String,
        val description: String
)

fun esc(vararg scalar: Any?) = scalar
        .filter { it != null }
        .joinToString(separator = "")
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
