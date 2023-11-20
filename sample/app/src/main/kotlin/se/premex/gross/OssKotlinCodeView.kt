package se.premex.gross

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.githhub.usefulness.licensee.android.app.R
import io.github.usefulness.licensee.LicenseeForAndroid
import se.premex.gross.ui.OssView

@Composable
fun ProgrammaticOssView() {
    Column {
        Text(
            text = stringResource(id = R.string.programmatic),
            modifier = Modifier
                .padding(16.dp),
        )
        OssView(LicenseeForAndroid.artifacts)
    }
}
