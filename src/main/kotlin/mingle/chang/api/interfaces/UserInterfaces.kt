package mingle.chang.api.interfaces

import mingle.chang.api.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRespository : JpaRepository<User, String> {
    fun existsByAccount(account: String) : Boolean

    fun findFirstByAccountAndPassword(account: String, password: String): User?
}