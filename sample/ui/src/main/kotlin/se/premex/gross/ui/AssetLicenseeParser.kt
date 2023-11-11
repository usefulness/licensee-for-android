package se.premex.gross.ui

import android.content.res.AssetManager
import io.github.usefulness.licensee.core.Artifact
import io.github.usefulness.licensee.core.LicenseeParser
import io.github.usefulness.licensee.core.SpdxLicenses
import io.github.usefulness.licensee.core.UnknownLicenses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

class AssetLicenseeParser(private val assetManager: AssetManager) {
    suspend fun readFromAssets(): List<Artifact> = withContext(Dispatchers.IO) {
        val source = assetManager.open("licensee_artifacts.json").source().buffer()
        LicenseeParser.decode(source)
    }
}

internal fun List<UnknownLicenses>?.unknownToLicenses(): List<License> = orEmpty().map { unknown -> unknown.asLicense() }

internal fun UnknownLicenses.asLicense(): License = License(name, url)

internal fun List<SpdxLicenses>?.spdxToLicenses(): List<License> = orEmpty().map { spdx -> spdx.asLicense() }

internal fun SpdxLicenses.asLicense() = License(title = name, url = url)
