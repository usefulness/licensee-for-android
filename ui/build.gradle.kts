import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.github.ben.manes.versions)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
    kotlin("plugin.serialization") version "1.8.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
}

android {
    namespace = "se.premex.gross.oss"

    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
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
    api("se.premex.gross:core:1.0")
    implementation(libs.com.squareup.okio)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)

    implementation(libs.androidx.compose.material.material.icons.extended)

    implementation(libs.androidx.compose.animation)

    implementation(libs.androidx.compose.ui.ui.tooling)
    implementation(libs.androidx.compose.runtime.runtime.livedata)
    implementation(libs.androidx.compose.ui.ui.text)

    implementation(libs.androidx.core.core.ktx)
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test.junit)
    testImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
}
