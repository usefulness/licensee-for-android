package io.github.usefulness.licensee

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import se.premex.gross.ui.AssetLicenseeParser

@RunWith(RobolectricTestRunner::class)
class PluginIntegrationTest {

    @Test
    fun checkGeneratedCode() {
        checkLoadedArtifacts(
            artifacts = LicenseeForAndroid.artifacts,
            isViewPager2Dependency = { groupId == "androidx.viewpager2" && artifactId == "viewpager2" },
        )
    }

    @Test
    fun checkAssets() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val source = runBlocking { AssetLicenseeParser(context.assets).readFromAssets() }

        checkLoadedArtifacts(
            artifacts = source,
            isViewPager2Dependency = { groupId == "androidx.viewpager2" && artifactId == "viewpager2" },
        )
    }
}
