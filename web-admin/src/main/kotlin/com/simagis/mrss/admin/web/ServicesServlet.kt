package com.simagis.mrss.admin.web

import com.simagis.mrss.MRSS
import io.swagger.client.Pair
import java.io.IOException
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
        MRSS.servicesManagementAPIsApi.allWebServices.forEach {
            writer.println(it)
            val href = """/services/${it.name}/${it.version}/swagger.json"""
            writer.println("""<a href="$href" target=_blank>$href</a>""")
            writer.println("<hr>")
        }
        writer.println("</pre>")
    }

    private fun printSwaggerJson(response: HttpServletResponse, name: String, version: String) {
        val apiClient = MRSS.apiClient
        val call = apiClient.buildCall(
                "/api/$name/$version/swagger.json",
                "GET",
                emptyList<Pair>(),
                null,
                emptyMap<String, String>(),
                emptyMap<String, Any>(),
                arrayOfNulls<String>(0),
                null
        )
        response.contentType = "text/plain"
        val data = apiClient.execute<String>(call, String::class.java).data
        response.writer.print(data)
    }

}
