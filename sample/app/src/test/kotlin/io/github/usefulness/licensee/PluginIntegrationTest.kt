package io.github.usefulness.licensee

import io.githhub.usefulness.licensee.android.app.BuildConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PluginIntegrationTest {

    @Test
    fun checkGeneratedCode() {
        assertThat(Licensee.artifacts).isNotEmpty()
        val viewPagers = Licensee.artifacts.filter { it.groupId == "androidx.viewpager2" && it.artifactId == "viewpager2" }

        @Suppress("KotlinConstantConditions")
        when (BuildConfig.BUILD_TYPE) {
            "release" -> assertThat(viewPagers).isEmpty()
            "debug" -> assertThat(viewPagers.single()).isEqualTo(
                Artifact(
                    groupId = "androidx.viewpager2",
                    artifactId = "viewpager2",
                    version = "1.0.0",
                    name = "AndroidX Widget ViewPager2",
                    spdxLicenses = listOf(
                        SpdxLicenses(
                            identifier = "Apache-2.0",
                            name = "Apache License 2.0",
                            url = "https://www.apache.org/licenses/LICENSE-2.0",
                        ),
                    ),
                    scm = Scm(url = "http://source.android.com"),
                    unknownLicenses = emptyList(),
                ),
            )

            else -> error("Not supported")
        }
        assertThat(Licensee.artifacts).isNotEmpty()
    }
}
