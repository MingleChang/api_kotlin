package mingle.chang.api.configurers

import mingle.chang.api.interceptors.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfigurer : WebMvcConfigurer {

    val authInterceptor : AuthInterceptor

    @Autowired
    var environment : Environment? = null

    @Autowired
    constructor(authInterceptor: AuthInterceptor) {
        this.authInterceptor = authInterceptor
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        val env = environment?.activeProfiles?.get(0) ?: "prod"
        if (env == "prod") {
            val registration = registry.addInterceptor(authInterceptor)
            registration.addPathPatterns("/**")
            registration.excludePathPatterns("/user/signup")
            registration.excludePathPatterns("/user/signin")
            super.addInterceptors(registry)
        }
    }

//    fun  buildConfig(): CorsConfiguration {
//        val corsConfiguration = CorsConfiguration()
//        corsConfiguration.addAllowedOrigin("*")
//        corsConfiguration.addAllowedMethod("*")
//        corsConfiguration.addAllowedHeader("*")
//        return corsConfiguration
//    }
//
//    @Bean
//    fun corsFilter(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", this.buildConfig())
//        return CorsFilter(source)
//    }
}