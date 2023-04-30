plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.8.10"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
}

buildscript {
    dependencies {
        classpath(libs.com.android.tools.build.gradle)
    }
}

dependencies {
    implementation(libs.com.android.tools.build.gradle)
    implementation(libs.org.jetbrains.kotlin.kotlin.gradle.plugin)
    implementation("com.squareup:kotlinpoet:1.12.0") {
        exclude(module = "kotlin-reflect")
    }

    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json.okio)
    implementation(libs.com.squareup.okio)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("se.premex.gross.licensee-assets") {
            id = "se.premex.gross.licensee-assets"
            group = "se.premex.gross"
            implementationClass = "se.premex.gross.CopyLicenseeReportToAssetsPlugin"
        }
    }
}
