package com.simagis.mrss

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 6/7/2017.
 */
fun main(args: Array<String>) {
    println(MRSS.call("PanelDetails",  "0.4", json { add("Pos", 123)}).ppString())
}