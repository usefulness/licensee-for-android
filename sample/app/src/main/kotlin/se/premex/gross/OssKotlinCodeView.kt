package se.premex.gross

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.githhub.usefulness.licensee.android.app.R
import io.github.usefulness.licensee.Licensee
import io.github.usefulness.licensee.core.Artifact
import io.github.usefulness.licensee.core.Scm
import io.github.usefulness.licensee.core.SpdxLicenses
import io.github.usefulness.licensee.core.UnknownLicenses
import se.premex.gross.ui.OssView

@Composable
fun ProgrammaticOssView() {
    Column {
        Text(text = stringResource(id = R.string.programmatic))
        OssView(
            Licensee.artifacts.map { artifact ->
                Artifact(
                    name = artifact.name,
                    groupId = artifact.groupId,
                    artifactId = artifact.artifactId,
                    version = artifact.version,
                    scm = if (artifact.scm?.url != null) {
                        Scm(artifact.scm.url)
                    } else {
                        null
                    },
                    spdxLicenses = artifact.spdxLicenses.map {
                        SpdxLicenses(
                            identifier = it.identifier,
                            name = it.name,
                            url = it.url,
                        )
                    },
                    unknownLicenses = artifact.unknownLicenses.map {
                        UnknownLicenses(name = it.name, url = it.url)
                    },
                )
            },
        )
    }
}
