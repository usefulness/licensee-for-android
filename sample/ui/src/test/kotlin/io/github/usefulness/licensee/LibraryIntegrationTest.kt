package io.github.usefulness.licensee

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import example.generated.from.kotlin.library.LicenseeForAndroid
import io.github.usefulness.licensee.serialization.LicenseeParser
import okio.buffer
import okio.source
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LibraryIntegrationTest {

    @Test
    fun checkGeneratedCode() {
        assertThat(LicenseeForAndroid.artifacts).isNotEmpty()
    }

    @Test
    fun checkResource() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val source = context.assets.open("ui_library_asset.json").source().buffer()
        val artifacts = LicenseeParser.decode(source)

        assertThat(artifacts).isNotEmpty()
        assertThat(artifacts.filter { it.groupId == "androidx.activity" }).isNotEmpty()
    }
}
