package com.simagis.mrss.admin.web

import com.simagis.mrss.MRSS
import com.simagis.mrss.ppString
import com.simagis.mrss.toJsonArray
import com.simagis.mrss.toJsonObject
import java.io.IOException
import javax.json.JsonObject
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 *
 * Created by alexei.vylegzhanin@gmail.com on 5/21/2017.
 */
@WebServlet(name = "ServicesServlet", urlPatterns = arrayOf("/services/*"))
class ServicesServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val pathInfo = request.pathInfo
        val path by lazy { pathInfo.split("/") }
        when {
            pathInfo == "/" -> printList(response)
            path.size == 4 && path[3] == "swagger.json" -> printSwaggerJson(response, path[1], path[2])
        }
    }

    private fun printList(response: HttpServletResponse) {
        val writer = response.writer
        writer.println("<pre>")
        MRSS.newRequest("/services").execute().run {
            body()?.string()?.toJsonArray()?.forEach {
                if (it is JsonObject) {
                    writer.println(it.ppString())
                    val href = """/services/${it.getString("name")}/${it.getString("version")}/swagger.json"""
                    writer.println("""<a href="$href" target=_blank>$href</a>""")
                    writer.println("<hr>")
                }
            }

        }
        writer.println("</pre>")
    }

    private fun printSwaggerJson(response: HttpServletResponse, name: String, version: String) {
        MRSS.newRequest("/api/$name/$version/swagger.json").execute().run {
            response.contentType = "text/plain"
            body()?.string()?.toJsonObject()?.ppString()?.let {
                response.writer.print(it)
            }
        }
    }

}
