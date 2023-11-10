package se.premex.gross.ui

import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import okio.buffer
import okio.source
import se.premex.gross.core.Artifact
import se.premex.gross.core.LicenseParser
import se.premex.gross.core.SpdxLicenses
import se.premex.gross.core.UnknownLicenses

class AssetLicenseParser(private val assetManager: AssetManager) : LicenseParser {
    @ExperimentalSerializationApi
    suspend fun readFromAssets(): List<Artifact> = withContext(Dispatchers.IO) {
        val source = assetManager.open("artifacts.json").source().buffer()
        decode(source)
    }
}

internal fun List<UnknownLicenses>?.unknownToLicenses(): List<License> = orEmpty().map { unknown -> unknown.asLicense() }

internal fun UnknownLicenses.asLicense(): License = License(name, url)

internal fun List<SpdxLicenses>?.spdxToLicenses(): List<License> = orEmpty().map { spdx -> spdx.asLicense() }

internal fun SpdxLicenses.asLicense() = License(title = name, url = url)
