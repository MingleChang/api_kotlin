package mingle.chang.api.entities

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
data class User (
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", length = 34)
    var id: String = "",

    @Column(name = "avatar")
    var avatar: String = "",

    @Column(name = "account", unique = true)
    var account: String = "",

    @Column(name = "password")
    var password: String = "",

    @Column(name = "real_name")
    var realName: String = "",

    @Column(name = "nick_name")
    var nickName: String = "",

    @Column(name = "mobile")
    var mobile: String = "",

    @Column(name = "phone")
    var phone: String = "",

    @Column(name = "email")
    var email: String = "",

    @Column(name = "created_date")
    @CreatedDate
    var createdDate: Date? = null,

    @Column(name = "modified_date")
    @LastModifiedDate
    var modifiedDate: Date? = null
)

