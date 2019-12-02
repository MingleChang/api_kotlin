package mingle.chang.api.controllers

import mingle.chang.api.entities.Token
import mingle.chang.api.entities.User
import mingle.chang.api.interfaces.TokenRespository
import mingle.chang.api.interfaces.UserRespository
import mingle.chang.api.models.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*

@RestController
class UserController  {
    private final var userRespository: UserRespository
    private final var tokenRespository: TokenRespository
    @Autowired
    constructor(userRespository: UserRespository, tokenRespository: TokenRespository) {
        this.userRespository = userRespository
        this.tokenRespository = tokenRespository
    }
    /*
    * 注册
    * */
    @PostMapping("/user/signup")
    fun signUp(@RequestParam("account") account: String, @RequestParam("password") password: String) : Any {
        try {
            val exists = this.userRespository.existsByAccount(account)
            if (exists) {
                return Response(code = 400, message = "该账号已存在")
            }
            val user = User()
            user.account = account
            user.password = password
            val resultUser = this.userRespository.save(user)
            val token = Token(user = resultUser)
            val resultToken = this.tokenRespository.save(token)
            val result = mutableMapOf<String, Any>()
            result["token"] = resultToken.id
            result["userId"] = resultUser.id
            result["account"] = resultUser.account
            result["realName"] = resultUser.realName
            result["nickName"] = resultUser.nickName
            result["mobile"] = resultUser.mobile
            result["email"] = resultUser.email
            return Response(result)
        }catch (e: Exception) {
            return Response(code = 400, message = e.toString())
        }
    }
    /*
    * 登陆
    * */
    @PostMapping("/user/signin")
    fun signIn(@RequestParam("account") account: String, @RequestParam("password") password: String) : Any {
        try {
            val user = this.userRespository.findFirstByAccountAndPassword(account, password)
                    ?: return Response(code = 400, message = "账号或密码错误")
            val token = Token(user = user)
            val resultToken = this.tokenRespository.save(token)
            val result = mutableMapOf<String, Any>()
            result["token"] = resultToken.id
            result["userId"] = user.id
            result["account"] = user.account
            result["realName"] = user.realName
            result["nickName"] = user.nickName
            result["mobile"] = user.mobile
            result["email"] = user.email
            return Response(result)
        }catch (e: Exception) {
            return Response(code = 400, message = e.toString())
        }
    }
}

