package mingle.chang.api.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Embeddable
class GuessKey : Serializable {
    @Column(name = "word")
    var word: String = ""

    @Column(name = "language")
    var language: String = ""
}

@Entity
@Table(name = "guess")
@EntityListeners(AuditingEntityListener::class)
data class Guess (
        @EmbeddedId
        var id: GuessKey = GuessKey(),

        @Column(name = "category")
        var category: String = "",

        @Column(name = "version")
        var version: String = (System.currentTimeMillis()).toString(),

        @Column(name = "modified_date")
        @LastModifiedDate
        var modifiedDate: Date? = null
) {
    var word: String
    set(value) {
        id.word = value
    }
    get() = id.word

    var language: String
    set(value) {
        id.language = value
    }
    get() = id.language
}