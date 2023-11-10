plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
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

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
}
