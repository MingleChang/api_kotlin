package mingle.chang.api.interfaces

import mingle.chang.api.entities.App
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AppRespository : JpaRepository<App, String> {
    @Query("SELECT a FROM App a WHERE createdDate IN(SELECT MAX(createdDate) FROM App GROUP BY packageId)")
    fun findGroupPackageId() : List<App>;

    @Query("SELECT a FROM App a WHERE createdDate IN(SELECT MAX(createdDate) FROM App WHERE packageId=:packageId GROUP BY platform)")
    fun findGroupPlatformByPackageId(packageId: String): List<App>;

    @Query("SELECT a FROM App a WHERE createdDate IN(SELECT MAX(createdDate) FROM App WHERE packageId=:packageId AND platform=:platform GROUP BY environment)")
    fun findGroupEnvironmentByPackageIdAndPlatform(packageId: String, platform: String): List<App>;

    @Query("SELECT a FROM App a WHERE createdDate IN(SELECT MAX(createdDate) FROM App WHERE packageId=:packageId AND platform=:platform AND environment=:environment GROUP BY version)")
    fun findGroupVersionByPackageIdAndPlatformAndEnvironment(packageId: String, platform: String, environment: String): List<App>;

    @Query("SELECT * FROM app WHERE if(:packageId!='',package_id=:packageId,1=1) AND if(:platform!='',platform=:platform,1=1) AND if(:environment!='',environment=:environment,1=1) AND if(:version!='',version=:version,1=1)", nativeQuery=true)
    fun findByPackageIdAndPlatformAndEnvironmentAndVersion(packageId: String, platform: String, environment: String, version: String): List<App>;

    @Query("SELECT * From app WHERE DATEDIFF(NOW(), modified_date)>=:days", nativeQuery=true)
    fun findUnusedOverDays(days: Long): List<App>;
}