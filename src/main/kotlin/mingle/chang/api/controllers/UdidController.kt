package mingle.chang.api.controllers

import mingle.chang.api.models.Response
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import javax.servlet.http.HttpServletRequest

@RestController
class UdidController {
    @GetMapping("/udid/config")
    fun getMobileConfig(): Any {
        try {
            val classPathResource = ClassPathResource("public/udid.mobileconfig")
            val inputStream = classPathResource.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val content = StringBuilder()
            var line: String? = null
            while ({ line = bufferedReader.readLine();line }() != null) {
                content.append(line)
            }
            val body = content.toString()
            val headers = HttpHeaders()
            headers.set("Content-Type", "application/octet-stream")
            headers.set("Content-Disposition", "attchement;filename=udid.mobileconfig");
            return ResponseEntity(body, headers, HttpStatus.OK)
        }catch (e: Exception) {
            val headers = HttpHeaders()
            val response = Response(code = 400, message = e.toString())
            return ResponseEntity(
                    response,
                    headers,
                    HttpStatus.OK
            )
        }
    }

    @PostMapping("/udid/receive")
    fun receiveUdid(request: HttpServletRequest) : Any {
        val inputStream = request.inputStream
//        val info = PropertyListParser.parse(inputStream) as NSDictionary
//        val a = info.objectForKey("a") as NSString

        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val content = StringBuilder()
        var line: String? = null
        while ({ line = bufferedReader.readLine();line }() != null) {
            content.append(line)
        }
        val body = content.toString()
        val redirect =  RedirectView("https://app.minglechang.com/udid?body=" + body)
        redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY)
        return redirect
    }
}