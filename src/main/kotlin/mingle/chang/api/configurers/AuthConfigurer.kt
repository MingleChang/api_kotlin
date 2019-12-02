package mingle.chang.api.configurers

import mingle.chang.api.interceptors.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfigurer : WebMvcConfigurer {
    val authInterceptor : AuthInterceptor

    @Autowired
    constructor(authInterceptor: AuthInterceptor) {
        this.authInterceptor = authInterceptor
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
//        val registration = registry.addInterceptor(authInterceptor)
//        registration.addPathPatterns("/**")
//        registration.excludePathPatterns("/user/signup")
//        registration.excludePathPatterns("/user/signin")
        super.addInterceptors(registry)
    }
}