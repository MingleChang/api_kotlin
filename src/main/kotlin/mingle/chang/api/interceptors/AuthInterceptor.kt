package mingle.chang.api.interceptors

import com.fasterxml.jackson.databind.ObjectMapper
import mingle.chang.api.interfaces.TokenRespository
import mingle.chang.api.interfaces.UserRespository
import mingle.chang.api.models.Response
import mingle.chang.api.utils.TokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor : HandlerInterceptor {
    private final var userRespository: UserRespository
    private final var tokenRespository: TokenRespository
    @Autowired
    constructor(userRespository: UserRespository, tokenRespository: TokenRespository) {
        this.userRespository = userRespository
        this.tokenRespository = tokenRespository
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tokenId = request.getHeader("token")
        val (success, code, message) = TokenUtils.vaildTokenId(tokenId, this.tokenRespository)
        return if (!success) {
            val responseModel = Response(code = code, message = message)
            val mapper = ObjectMapper()
            val json = mapper.writeValueAsString(responseModel)
            response.contentType = "application/json"
            response.writer.println(json)
            false
        }else {
            super.preHandle(request, response, handler)
        }
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)
    }
}