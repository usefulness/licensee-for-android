package io.githhub.usefulness.licensee.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import se.premex.gross.AssetsOssView
import se.premex.gross.ProgrammaticOssView
import se.premex.gross.ui.theme.GrossTheme

enum class Views {
    Programmatic,
    AssetBased,
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            var selectedView by remember { mutableStateOf(Views.Programmatic) }

            GrossTheme {
                Scaffold(
                    bottomBar = {
                        BottomAppBar {
                            NavigationBarItem(
                                selected = selectedView == Views.Programmatic,
                                onClick = {
                                    selectedView = Views.Programmatic
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Create,
                                        contentDescription = stringResource(id = R.string.programmatic),
                                    )
                                },
                            )
                            NavigationBarItem(
                                selected = selectedView == Views.AssetBased,
                                onClick = {
                                    selectedView = Views.AssetBased
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.AddCircle,
                                        contentDescription = stringResource(id = R.string.assetBased),
                                    )
                                },
                            )
                        }
                    },
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        when (selectedView) {
                            Views.Programmatic -> ProgrammaticOssView()
                            Views.AssetBased -> AssetsOssView()
                        }
                    }
                }
            }
        }
    }
}
