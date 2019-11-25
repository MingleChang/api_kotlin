package mingle.chang.api.controllers

import mingle.chang.api.entities.App
import mingle.chang.api.interfaces.AppRespository
import mingle.chang.api.models.Response
import mingle.chang.api.utils.AppUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import sun.misc.IOUtils
import java.io.*
import java.sql.SQLDataException


@RestController
class AppController {
    @Value("\${appHost}")
    var appHost: String? = null

    @Value("\${appCachePath}")
    val appRootPath: String? = null

    private final var appRespository: AppRespository

    @Autowired
    constructor(appRespository: AppRespository) {
        this.appRespository = appRespository
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Shanghai")
    fun removeUnusedApps() {
        try {
            val apps = this.appRespository.findUnusedOverDays(3)
            for (app in apps) {
                val rootDirectoryFile = File(appRootPath)
                val fileName = app.fileName
                val file = File(rootDirectoryFile, fileName)
                if (file.exists()) {
                    file.delete()
                }
                this.appRespository.delete(app)
            }
        }catch (e: Exception) {

        }
    }

    @GetMapping("/app/packageids")
    fun getPackageIds() : Response {
        return try {
            val list = this.appRespository.findGroupPackageId()
            val response = Response(list)
            response
        }catch (e: Exception) {
            val response = Response(code = 400, message = e.toString())
            response
        }
    }

    @GetMapping("/app/platforms")
    fun getPlatforms(@RequestParam("packageid") packageId : String) : Response {
        return try {
            val list = this.appRespository.findGroupPlatformByPackageId(packageId)
            val response = Response(list)
            response
        }catch (e: Exception) {
            val response = Response(code = 400, message = e.toString())
            response
        }
    }

    @GetMapping("/app/environments")
    fun getEnvironments(@RequestParam("packageid") packageId : String, @RequestParam("platform") platform : String) : Response {
        return try {
            val list = this.appRespository.findGroupEnvironmentByPackageIdAndPlatform(packageId, platform)
            val response = Response(list)
            response
        }catch (e: Exception) {
            val response = Response(code = 400, message = e.toString())
            response
        }
    }

    @GetMapping("/app/versions")
    fun getVersions(@RequestParam("packageid") packageId : String, @RequestParam("platform") platform : String, @RequestParam("environment") environment : String) : Response {
        return try {
            val list = this.appRespository.findGroupVersionByPackageIdAndPlatformAndEnvironment(packageId, platform, environment)
            val response = Response(list)
            response
        }catch (e: Exception) {
            val response = Response(code = 400, message = e.toString())
            response
        }
    }

    @GetMapping("/app/list")
    fun getApps(@RequestParam("packageid") packageId : String?, @RequestParam("platform") platform : String?, @RequestParam("environment") environment : String?, @RequestParam("version") version : String?) : Response {
        return try {
            val packageId = if (packageId.isNullOrBlank()) {
                ""
            }else {
                packageId
            }
            val platform = if (platform.isNullOrBlank()) {
                ""
            }else {
                platform
            }
            val environment = if (environment.isNullOrBlank()) {
                ""
            }else {
                environment
            }
            val version = if (version.isNullOrBlank()) {
                ""
            }else {
                version
            }
            val list = this.appRespository.findByPackageIdAndPlatformAndEnvironmentAndVersion(packageId, platform, environment, version)
            val response = Response(list)
            response
        }catch (e: Exception) {
            val response = Response(code = 400, message = e.toString())
            response
        }
    }

    @GetMapping("/app/{id}")
    fun getAppById(@PathVariable("id") id: String) : Response {
        return try {
            val app = this.appRespository.getOne(id)
            val response = Response(app)
            response
        }catch (e: Exception) {
            val response = Response(code = 400, message = e.toString())
            response
        }
    }

    @PostMapping("/app/upload")
    fun upload(@RequestParam("file") file: MultipartFile, @RequestParam("platform") platform: String, @RequestParam("environment") environment: String) : Response {
        val platform = platform
        val environment = environment.toUpperCase()
        if (platform != "iOS" && platform != "Android") {
            return Response(code = 500, message = "platform must be 'iOS' or 'Android'")
        }
        val rootDirectoryFile = File(appRootPath)
        val tmpDirectoryFile = File(rootDirectoryFile, "tmp")
        if (!tmpDirectoryFile.exists()) {
            tmpDirectoryFile.mkdirs()
        }

        val tmpFileName = UUID.randomUUID().toString()
        val tmpFile = File(tmpDirectoryFile, tmpFileName)
        file.transferTo(tmpFile)

        val app = App()
        if (platform == "iOS") {
            val result = AppUtils.parseIpaFile(tmpFile) ?: return Response(code = 500, message = "parse failed")
            app.packageId = result["bundleId"].toString()
            app.name = result["name"].toString()
            app.displayName = result["displayName"].toString()
            app.version = result["version"].toString()
            app.buildVersion = result["buildVersion"].toString()
            app.platform = platform
            app.environment = environment
        }else {
            val result = AppUtils.parseApkFile(tmpFile) ?: return Response(code = 500, message = "parse failed")
            app.packageId = result["bundleId"].toString()
            app.name = result["name"].toString()
            app.displayName = result["displayName"].toString()
            app.version = result["version"].toString()
            app.buildVersion = result["buildVersion"].toString()
            app.platform = platform
            app.environment = environment
        }

        val fileName = app.fileName
        val appFile = File(rootDirectoryFile, fileName)
        if (appFile.exists()) {
            appFile.delete()
        }
        tmpFile.copyTo(appFile)
        tmpFile.delete()
        val resultApp = appRespository.save(app)
        return Response(resultApp)
    }

    @GetMapping("/app/download/{id}")
    fun download(@PathVariable("id") id: String): ResponseEntity<Any> {
        try {
            val app = this.appRespository.getOne(id)
            val rootDirectoryFile = File(appRootPath)
            val fileName = app.fileName
            val file = File(rootDirectoryFile, fileName)

            if (!file.exists()) {
                val headers = HttpHeaders()
                val response = Response(code = 404, message = "file not found")
                return ResponseEntity(response, headers, HttpStatus.OK)
            }

            var body: ByteArray? = null
            val stream = FileInputStream(file)
            body = ByteArray(stream.available())
            stream.read(body)
            val headers = HttpHeaders()
            headers.add("Content-Disposition", "attchement;filename=" + file.name)
            val statusCode = HttpStatus.OK
            app.downloadCount++
            this.appRespository.save(app)
            return ResponseEntity(body, headers, statusCode)
        }catch(e: Exception) {
            val headers = HttpHeaders()
            val response = Response(code = 400, message = e.toString())
            return ResponseEntity(response, headers, HttpStatus.OK)
        }
    }

    @GetMapping("/app/plist/{id}")
    fun plist(@PathVariable("id") id: String): ResponseEntity<Any> {
        try {
            val classPathResource = ClassPathResource("public/manifest_temp.plist")
            val inputSteam = classPathResource.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputSteam))
            val content = StringBuilder()
            var line: String? = null
            while ({ line = bufferedReader.readLine();line }() != null) {
                content.append(line)
            }
            val app = this.appRespository.getOne(id)
            val url = app.fileAddress(this.appHost!!)
            val bundleId = app.packageId
            val bundleVersion = app.version
            val bundleName = app.name
            val body = content.toString().replace("{{url}}", url).replace("{{bundleId}}", bundleId).replace("{{bundleVersion}}", bundleVersion).replace("{{bundleName}}", bundleName)

            val headers = HttpHeaders()
            headers.set("Content-Type", "application/octet-stream")
            return ResponseEntity(body, headers, HttpStatus.OK)
        }catch (e: Exception) {
            val headers = HttpHeaders()
            val response = Response(code = 400, message = e.toString())
            return ResponseEntity(
                    response,
                    headers,
                    HttpStatus.OK
            )
        }
    }
}