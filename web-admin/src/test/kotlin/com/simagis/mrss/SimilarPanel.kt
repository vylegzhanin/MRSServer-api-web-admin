package com.simagis.mrss

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 8/10/2017.
 */
fun main(args: Array<String>) {
    val request = json {
        add("ResultTest", array {
            add("B329")
        })
    }
    println(request)
    println(MRSS.call("SimilarPanel",  "0.5", request).ppString())
}