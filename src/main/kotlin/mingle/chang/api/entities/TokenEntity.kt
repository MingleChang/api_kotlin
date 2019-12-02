package mingle.chang.api.entities

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

enum class TokenInvalidReason {
        EXPIRED,
        REFRESH
}

@Entity
@Table(name = "token")
@EntityListeners(AuditingEntityListener::class)
data class Token (
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(name = "id", length = 34)
        var id: String = "",

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "TOKEN_USER_ID_FK"))
        var user: User,

        @Column(name = "invalid")
        var invalid: Boolean = false,

        /*
        * 失效原因
        * */
        @Column(name = "reason")
        var reason: TokenInvalidReason? = null,

        @Column(name = "expired_date")
        var expiredDate: Date = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000),

        @Column(name = "created_date")
        @CreatedDate
        var createdDate: Date? = null,

        @Column(name = "modified_date")
        @LastModifiedDate
        var modifiedDate: Date? = null
)