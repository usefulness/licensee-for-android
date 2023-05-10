// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"

}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
}

group = "se.premex.gross"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

dependencies {
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json.okio)
    implementation(libs.com.squareup.okio)

    testImplementation(libs.org.jetbrains.kotlin.kotlin.test.junit)
    testImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}
