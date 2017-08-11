package com.simagis.mrss

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 8/10/2017.
 */
fun main(args: Array<String>) {
    println(MRSS.call("ResultTestPay",  "0.5", json { }).ppString())
}