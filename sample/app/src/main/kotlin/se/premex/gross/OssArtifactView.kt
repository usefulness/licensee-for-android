package se.premex.gross

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.githhub.usefulness.licensee.android.app.R
import se.premex.gross.ui.AssetLicenseeParser
import se.premex.gross.ui.ErrorView
import se.premex.gross.ui.LoadingView
import se.premex.gross.ui.OssView
import se.premex.gross.ui.OssViewState
import se.premex.gross.ui.State

@Composable
fun AssetsOssView() {
    val assetManager = LocalContext.current.assets

    val licenseParser = remember { AssetLicenseeParser(assetManager) }

    val uiState = remember { mutableStateOf<OssViewState>(State.Loading) }

    LaunchedEffect(assetManager) {
        uiState.value = runCatching { licenseParser.readFromAssets() }.fold(
            onSuccess = { State.Success(data = it) },
            onFailure = { State.Failed(errorMessage = it.localizedMessage.orEmpty()) },
        )
    }

    when (val state = uiState.value) {
        State.Loading -> LoadingView(stringResource(id = R.string.loading))

        is State.Failed -> ErrorView(stringResource(id = R.string.error), state.errorMessage)

        is State.Success -> {
            Column {
                Text(
                    text = stringResource(id = R.string.assetBased),
                    modifier = Modifier
                        .padding(16.dp),
                )

                OssView(state.data)
            }
        }
    }
}
