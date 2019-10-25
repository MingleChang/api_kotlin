package mingle.chang.api.utils

import com.dd.plist.NSDictionary
import com.dd.plist.NSString
import com.dd.plist.PropertyListParser
import net.dongliu.apk.parser.ApkFile
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class AppUtils {
    companion object{
        fun parseIpaFile(file: File): Map<String, String>? {
            return try {
                val zip = ZipFile(file)
                for (zipEntry: ZipEntry in zip.entries()) {
                    if (zipEntry.name.toLowerCase().contains(".app/info.plist")) {
                        val steam = zip.getInputStream(zipEntry)
                        val ipaInfo = PropertyListParser.parse(steam) as NSDictionary
                        val bundleId = ipaInfo.objectForKey("CFBundleIdentifier") as NSString
                        val name = ipaInfo.objectForKey("CFBundleName") as NSString
                        val displayName = ipaInfo.objectForKey("CFBundleDisplayName") as NSString
                        val version = ipaInfo.objectForKey("CFBundleShortVersionString") as NSString
                        val buildVersion = ipaInfo.objectForKey("CFBundleVersion") as NSString
                        return mapOf(
                                "bundleId" to bundleId.toString(),
                                "name" to name.toString(),
                                "displayName" to displayName.toString(),
                                "version" to version.toString(),
                                "buildVersion" to buildVersion.toString()
                        )
                        break;
                    }
                }
                null
            }catch (e: Exception) {
                null
            }
        }

        fun parseApkFile(file: File): Map<String, String>? {
            return try {
                val apkFile = ApkFile(file)
                val apkMeta = apkFile.apkMeta;
                mapOf(
                        "bundleId" to apkMeta.packageName,
                        "name" to apkMeta.name,
                        "displayName" to apkMeta.label,
                        "version" to apkMeta.versionName,
                        "buildVersion" to apkMeta.versionCode.toString()
                )
            }catch (e: Exception) {
                null
            }
        }
    }
}