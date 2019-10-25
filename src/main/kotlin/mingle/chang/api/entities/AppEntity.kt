package mingle.chang.api.entities

import mingle.chang.api.utils.AppUtils
import org.hibernate.annotations.GenericGenerator
import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.persistence.*
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "app")
@EntityListeners(AuditingEntityListener::class)
data class App (
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(name = "id", length = 34)
        var id: String = "",

        @Column(name = "name")
        var name: String = "",

        @Column(name = "display_name")
        var displayName : String = "",

        @Column(name = "package_id")
        var packageId: String = "",

        @Column(name = "version")
        var version: String = "",

        @Column(name = "build_version")
        var buildVersion: String = "",

        @Column(name = "platform")
        var platform: String = "",

        @Column(name = "environment")
        var environment: String = "",

        @Column(name = "created_date")
        @CreatedDate
        var createdDate: Date? = null,

        @Column(name = "modified_date")
        @LastModifiedDate
        var modifiedDate: Date? = null
){
        fun fileName() : String {
                return this.packageId + "_" + this.platform + "_" + this.environment + "_" + this.version + "_" + this.buildVersion
        }

        fun fileAddress(host: String): String {
                return host + "/app/download/" + this.id
        }
}