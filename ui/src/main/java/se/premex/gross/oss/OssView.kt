@file:OptIn(ExperimentalFoundationApi::class)

package se.premex.gross.oss

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OssView() {

    val licenseParser = remember { LicenseParser() }

    val assetManager = LocalContext.current.assets

    val uiState = remember { mutableStateOf(OssViewState()) }

    LaunchedEffect(key1 = assetManager) {
        val viewData: List<ViewData> = licenseParser.readFromAssets(assetManager).map {
            val licenses = it.spdxLicenses.orEmpty()
                .map { spdx -> License(spdx.name, spdx.url) } +
                    it.unknownLicenses.orEmpty()
                        .map { unknown -> License(unknown.name, unknown.url) }

            val nameOrPackage = it.name ?: (it.groupId + ":" + it.groupId)
            ViewData(
                nameOrPackage,
                licenses
            )
        }.sortedBy { it.title }
        uiState.value = OssViewState(loadingState = State.Success(data = viewData))
    }

    when (val state = uiState.value.loadingState) {
        is State.Failed -> ErrorView(stringResource(id = R.string.error))
        is State.Loading -> LoadingView(stringResource(id = R.string.loading))
        is State.Success -> OssView(state.data)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OssView(artifacts: List<ViewData>) {
    val licenses: SnapshotStateList<License> = remember { mutableStateListOf() }
    var alertTitle by remember { mutableStateOf("") }
    LicenseSelector(alertTitle, licenses) {
        licenses.clear()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp),
                    text = stringResource(id = R.string.oss_description)

                )
            }
        }

        val grouped: Map<String, List<ViewData>> =
            artifacts.groupBy { it.title[0].uppercaseChar().toString() }
        grouped.forEach { (title, list) ->
            stickyHeader {
                CharacterHeader(title)
            }
            items(list) { artifact ->
                ListItem(
                    headlineText = {
                        Text(text = artifact.title)
                    },
                    modifier = Modifier.clickable {
                        alertTitle = artifact.title
                        licenses.addAll(artifact.licenses)
                    }
                )
            }
        }
    }
}

@Composable
fun CharacterHeader(initial: String) {
    Text(
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 4.dp
        ),
        text = initial
    )
}

@Preview(showSystemUi = true)
@Composable
fun LicenseSelectorPreview() {
    Column(Modifier.fillMaxSize()) {
        LicenseSelector("Licenses", listOf(License("aaa", "http://google.se"))) {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseSelector(title: String, licenses: List<License>, close: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    if (licenses.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                close()
            },
            title = {
                Text(text = title)
            },
            text = {
                Column {
                    licenses.forEach { license ->
                        ListItem(
                            headlineText = {
                                Text(text = license.title)
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.Link,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {
                                uriHandler.openUri(license.url)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = close
                ) {
                    Text(stringResource(id = R.string.close))
                }
            },
        )
    }
}
