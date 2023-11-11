package io.github.usefulness.licensee

import io.githhub.usefulness.licensee.android.app.BuildConfig
import org.assertj.core.api.Assertions.assertThat

internal fun <T> checkLoadedArtifacts(artifacts: List<T>, isViewPager2Dependency: T.() -> Boolean) {
    assertThat(artifacts).isNotEmpty()
    val viewPagers = artifacts.filter { it.isViewPager2Dependency() }

    @Suppress("KotlinConstantConditions")
    when (BuildConfig.BUILD_TYPE) {
        "release" -> assertThat(viewPagers).isEmpty()
        "debug" -> assertThat(viewPagers.single().toString()).isEqualTo(
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
            ).toString(),
        )

        else -> error("Not supported")
    }
}
