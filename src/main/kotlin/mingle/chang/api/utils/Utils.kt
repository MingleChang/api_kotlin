package mingle.chang.api.utils

import javax.servlet.http.HttpServletRequest

class Utils {
    companion object {
        fun getRemoteAddress(request: HttpServletRequest) : String {
            var remoteAddress = request.getHeader("x-forwarded-for")
            if (remoteAddress.isNullOrBlank()) {
                remoteAddress = request.getHeader("Proxy-Client-IP")
            }
            if (remoteAddress.isNullOrBlank()) {
                remoteAddress = request.getHeader("WL-Proxy-Client-IP")
            }
            if (remoteAddress.isNullOrBlank()) {
                remoteAddress = request.remoteAddr
            }
            return remoteAddress
        }
    }
}