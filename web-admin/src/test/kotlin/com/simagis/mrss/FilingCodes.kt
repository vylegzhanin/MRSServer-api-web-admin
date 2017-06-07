package com.simagis.mrss

/**
 *
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */

fun main(args: Array<String>) {
    println(MRSS.call("FilingCodes",  "0.4", json {
        add("Test", "1015")
        add("Payer", "Cigna Health And Life Insurance Company")
    }).ppString())
}