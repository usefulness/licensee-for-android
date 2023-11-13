package se.premex.gross.ui

import android.content.res.AssetManager
import io.github.usefulness.licensee.Artifact
import io.github.usefulness.licensee.Scm
import io.github.usefulness.licensee.SpdxLicense
import io.github.usefulness.licensee.UnknownLicense
import io.github.usefulness.licensee.serialization.LicenseeParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

class AssetLicenseeParser(private val assetManager: AssetManager) {
    suspend fun readFromAssets(): List<Artifact> = withContext(Dispatchers.IO) {
        val source = assetManager.open("licensee_artifacts.json").source().buffer()
        val serialized = LicenseeParser.decode(source)

        serialized.map { artifact ->
            Artifact(
                groupId = artifact.groupId,
                artifactId = artifact.artifactId,
                version = artifact.version,
                name = artifact.name,
                spdxLicenses = artifact.spdxLicenses.map { license ->
                    SpdxLicense(
                        identifier = license.identifier,
                        name = license.name,
                        url = license.url,
                    )
                },
                scm = artifact.scm?.let { scm -> Scm(url = scm.url) },
                unknownLicenses = artifact.unknownLicenses.map { license ->
                    UnknownLicense(
                        name = license.name,
                        url = license.url,
                    )
                },
            )
        }
    }
}
