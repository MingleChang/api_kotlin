package mingle.chang.api.utils

import mingle.chang.api.entities.TokenInvalidReason
import mingle.chang.api.interfaces.TokenRespository

class TokenUtils {
    companion object {
        fun vaildTokenId(id: String?, tokenRespository: TokenRespository) : Triple<Boolean, Int, String> {
            if (id.isNullOrBlank()) {
                return Triple(false, 400, "缺少token参数")
            }else {
                val token = tokenRespository.findFirstById(id) ?: return Triple(false, 400,"token不存在")

                if (token.invalid) {
                    return Triple(false, 401, "token已失效")
                }
            }
            return Triple(true, 200, "")
        }
    }
}