package mingle.chang.api.interfaces

import mingle.chang.api.entities.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRespository : JpaRepository<Token, String> {
    fun findFirstById(id: String) : Token?
}