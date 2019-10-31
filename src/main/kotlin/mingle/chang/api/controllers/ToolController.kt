package mingle.chang.api.controllers

import mingle.chang.api.models.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest

@RestController
class ToolController {
    @GetMapping("/tools/ip")
    fun getIp(request: HttpServletRequest, @RequestParam("ip") ip: String?, @RequestParam("lang") lang: String?): Response {
        return try {
            val ipAddress: String = if (ip.isNullOrBlank()) {
                var remoteAddress = request.getHeader("x-forwarded-for")
                if (remoteAddress.isNullOrBlank()) remoteAddress = request.getHeader("Proxy-Client-IP")

                if (remoteAddress.isNullOrBlank()) remoteAddress = request.getHeader("WL-Proxy-Client-IP")

                if (remoteAddress.isNullOrBlank()) remoteAddress = request.remoteAddr
                remoteAddress
            }else {
                ip
            }
            val language: String = if (lang.isNullOrBlank()) {
                "zh-CN"
            }else {
                lang
            }
            val urlString = "http://ip-api.com/json/$ipAddress?lang=$language"
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject(urlString, Map::class.java)
            val status = result?.get("status")
            if (status == "success") {
                Response(result)
            }else {
                Response(result, code = 500, message = "failed")
            }
        }catch(e: RestClientException) {
            Response(code = 500, message = "failed")
        }catch (e: Exception) {
            Response(code = 500, message = "failed")
        }
    }
}