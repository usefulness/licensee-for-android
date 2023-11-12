package io.github.usefulness.licensee.core

import example.generated.from.kotlin.library.Licensee as LibraryLicensee
import org.junit.jupiter.api.Test
import example.generated.app_rules.from.kotlin.library.Licensee as AppLicensee
import org.assertj.core.api.Assertions.assertThat

class PluginIntegrationTest {

    @Test
    fun checkAppLicenses() {
        assertThat(AppLicensee.artifacts).isNotEmpty()
        assertThat(AppLicensee.artifacts.filter { it.groupId == "androidx.viewpager2" && it.artifactId == "viewpager2" }).hasSize(1)
    }

    @Test
    fun checkLibraryLicenses() {
        assertThat(LibraryLicensee.artifacts).isNotEmpty()
        assertThat(LibraryLicensee.artifacts.filter { it.groupId == "androidx.viewpager2" }).hasSize(0)
        assertThat(LibraryLicensee.artifacts.size).isNotEqualTo(AppLicensee.artifacts.size)
    }

    @Test
    fun checkLibraryResource() {
        val fileContent = readResource("licensee_artifacts.json").decodeToString()

        check(fileContent.contains("com.squareup.okio"))
    }
}
