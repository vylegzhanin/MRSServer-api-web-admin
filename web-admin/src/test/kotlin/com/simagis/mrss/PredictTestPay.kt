package com.simagis.mrss

/**
 *
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */

fun main(args: Array<String>) {
    println(MRSS.call("PredictTestPay", "0.1", json {
        add("in_prn", "United Healthcare Insurance Company")
        add("in_test", "7702")
        add("in_dx", "Z0000")
        add("in_ptnG", "F")
        add("in_ptnAge", 60)
        add("in_fCode", "16")
    }).ppString())
}
