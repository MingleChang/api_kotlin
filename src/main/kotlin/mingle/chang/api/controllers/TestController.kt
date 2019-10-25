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
}