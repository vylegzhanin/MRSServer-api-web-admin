package com.simagis.mrss

/**
 *
 * Created by alexei.vylegzhanin@gmail.com on 5/23/2017.
 */

fun main(args: Array<String>) {
    println(MRSS.call("ListPayers",  "0.1", json { add("Test", "J500") }).ppString())
}