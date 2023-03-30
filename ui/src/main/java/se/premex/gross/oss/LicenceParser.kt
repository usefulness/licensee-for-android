package se.premex.gross.oss

import android.content.res.AssetManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class SpdxLicenses(val identifier: String, val name: String, val url: String)

@Serializable
data class Scm(val url: String)

@Serializable
data class UnknownLicenses(val name: String, val url: String)

@Serializable
data class Artifact(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String? = null,
    val spdxLicenses: List<SpdxLicenses>? = null,
    val scm: Scm? = null,
    val unknownLicenses: List<UnknownLicenses>? = null,
)

class LicenseParser {
    suspend fun readFromAssets(assetManager: AssetManager): List<Artifact> {
        val readText = assetManager.open("artifacts.json").bufferedReader().readText()
        return decodeString(readText)
    }

    internal suspend fun decodeString(artifacts: String): List<Artifact> {
        return Json.decodeFromString(artifacts)
    }
}
