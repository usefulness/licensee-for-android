package se.premex.gross

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import se.premex.gross.ui.theme.GrossTheme

enum class Views {
    Programmatic, AssetBased
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val selectedView = remember { mutableStateOf(Views.Programmatic) }
            GrossTheme {
                Scaffold(
                    bottomBar = {
                        BottomAppBar {
                            NavigationBarItem(
                                selected = false,
                                onClick = {
                                    selectedView.value = Views.Programmatic
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Create,
                                        contentDescription = stringResource(id = R.string.programmatic)
                                    )
                                })
                            NavigationBarItem(
                                selected = false,
                                onClick = {
                                    selectedView.value = Views.AssetBased
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.AddCircle,
                                        contentDescription = stringResource(id = R.string.assetBased)
                                    )
                                })
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (selectedView.value) {
                            Views.Programmatic -> ProgrammaticOssView()
                            Views.AssetBased -> AssetsOssView()
                        }
                    }
                }
            }
        }
    }
}