package se.premex.gross.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.githhub.usefulness.licensee.android.ui.R

@Composable
fun ErrorView(errorString: String, errorMessage: String, modifier: Modifier = Modifier) = ErrorView(modifier = modifier) {
    Text(text = errorString)
    Text(text = errorMessage)
}

@Composable
fun ErrorView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = stringResource(
                id = R.string.error,
            ),
        )
        Spacer(Modifier.height(16.dp))
        content()
    }
}

@Preview
@Composable
private fun ErrorViewPreview() {
    ErrorView("ErrorString", "ErrorMessage")
}
