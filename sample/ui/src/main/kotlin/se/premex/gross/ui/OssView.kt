@file:OptIn(ExperimentalFoundationApi::class)

package se.premex.gross.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.githhub.usefulness.licensee.android.ui.R
import io.github.usefulness.licensee.Artifact

@Composable
fun OssView(artifacts: List<Artifact>, modifier: Modifier = Modifier) {
    var visibleDialog by remember { mutableStateOf<LicensesDialogData?>(value = null) }
    visibleDialog?.let { dialog ->
        LicenseSelector(
            dialogData = dialog,
            onDismissRequest = { visibleDialog = null },
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item(key = "header") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp),
                    text = stringResource(id = R.string.oss_description),
                )
            }
        }

        val viewData = artifacts.map { artifact ->
            val spdxViewLicenses = artifact.spdxLicenses.map { ViewLicense(title = it.name, url = it.url) }
            val unknown = artifact.unknownLicenses.map { ViewLicense(title = it.name, url = it.url) }

            val nameOrPackage = ("${artifact.name}\n(${artifact.groupId}:${artifact.artifactId}:${artifact.version})".trim())

            ViewArtifact(
                key = "${artifact.groupId}:${artifact.artifactId}",
                title = nameOrPackage,
                licenses = spdxViewLicenses + unknown,
            )
        }
            .sortedBy { (nameOrPackage, _) -> nameOrPackage }
        val grouped = viewData.groupBy { it.title[0].uppercaseChar().toString() }
        grouped.forEach { (title, list) ->
            stickyHeader {
                CharacterHeader(title)
            }
            items(
                items = list,
                key = { it.key },
            ) { artifact ->
                ListItem(
                    headlineContent = {
                        Text(text = artifact.title)
                    },
                    modifier = Modifier.clickable {
                        visibleDialog = LicensesDialogData(
                            title = artifact.title,
                            licenses = artifact.licenses,
                        )
                    },
                )
            }
        }
    }
}

private data class LicensesDialogData(
    val title: String,
    val licenses: List<ViewLicense>,
)

private data class ViewArtifact(
    val key: String,
    val title: String,
    val licenses: List<ViewLicense>,
)

private data class ViewLicense(
    val title: String?,
    val url: String?,
)

@Composable
fun CharacterHeader(initial: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 4.dp,
        ),
        text = initial,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun LicenseSelectorPreview() {
    Column(Modifier.fillMaxSize()) {
        LicenseSelector(
            dialogData = LicensesDialogData(
                title = "Foo Library",
                licenses = listOf(ViewLicense(title = "Foo License", url = "http://google.se")),
            ),
            onDismissRequest = { },
        )
    }
}

@Composable
private fun LicenseSelector(dialogData: LicensesDialogData, onDismissRequest: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = dialogData.title)
        },
        text = {
            Column {
                dialogData.licenses.forEach { license ->
                    ListItem(
                        headlineContent = {
                            license.title?.let { Text(text = it) }
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.Link,
                                contentDescription = null,
                            )
                        },
                        modifier = license.url?.let { url ->
                            Modifier.clickable {
                                uriHandler.openUri(url)
                            }
                        } ?: Modifier,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(id = R.string.close))
            }
        },
    )
}
