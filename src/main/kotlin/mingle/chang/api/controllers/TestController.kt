package mingle.chang.api.controllers

import mingle.chang.api.models.Response
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
        val paths = listOf<String>("/*")
        val appID = "USZ9R7RQAA.api.minglechang.com"
        val details = mapOf<String, Any>("appID" to appID, "paths" to paths)
        val apps = listOf<String>()
        val applinks = mapOf<String, Any>("apps" to apps, "details" to details)
        return mapOf<String, Any>("applinks" to applinks)
    }
}