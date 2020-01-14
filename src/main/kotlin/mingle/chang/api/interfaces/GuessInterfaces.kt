package mingle.chang.api.interfaces

import mingle.chang.api.entities.Guess
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface GuessRespository : JpaRepository<Guess, String> {
    @Query("SELECT new map(g.id.word as word, g.id.language as language, g.category as category, g.version as version) FROM Guess g WHERE g.version>:version")
    fun findAllByVersionAfter(version: String) : List<Map<String, Any>>

    @Query("SELECT new map(g.id.word as word, g.id.language as language, g.category as category, g.version as version) FROM Guess g WHERE (g.category=:category OR :category='') AND (g.id.language=:language OR :language='')", countQuery = "SELECT COUNT(g) FROM Guess g WHERE (g.category=:category OR :category='') AND (g.id.language=:language OR :language='')" )
    fun findAllByCategoryAndId_LanguagePageable(category: String, language: String, pageable: Pageable) : Page<Map<String, Any>>

    @Query("SELECT g.category FROM Guess g WHERE (:language='' OR g.id.language=:language) GROUP BY g.category")
    fun findGroupCategoryByLanguage(language: String): List<String>

    @Query("SELECT g.id.language FROM Guess g WHERE (:category='' OR g.category=:category) GROUP BY g.id.language")
    fun findGroupLanguageByCategory(category: String): List<String>
}