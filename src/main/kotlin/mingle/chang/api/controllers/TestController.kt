package mingle.chang.api.controllers

import mingle.chang.api.models.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test")
    fun test() : Response {
        return Response()
    }
    @GetMapping(value=["/.well-known/apple-app-site-association", "/apple-app-site-association"])
    fun appleAppSiteAssociation() : Any {
        val paths = listOf<String>("/iosuniversallink/*")
        val bundle = mapOf<String, Any>("paths" to paths)
        val appID = "USZ9R7RQAA.mingle.chang.AppLink"
        val details = mapOf<String, Any>(appID to bundle)
        val apps = listOf<String>()
        val applinks = mapOf<String, Any>("apps" to apps, "details" to details)
        val body = mapOf<String, Any>("applinks" to applinks)
        return  body
    }
}