plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.8.10"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.21.0"
    id("maven-publish")
    id("com.gladed.androidgitversion") version "0.4.14"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

androidGitVersion {
    tagPattern = "^v[0-9]+.*"
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

version = androidGitVersion.name().replace("v", "")
group = "se.premex"


gradlePlugin {
    plugins {
        create("gross") {
            id = "se.premex.gross"
            implementationClass = "se.premex.gross.GrossPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/premex-ab/gross"
    vcsUrl = "https://github.com/premex-ab/gross.git"
    description = "A plugin that generates a list of open source licenses you depend on"
    tags = mutableListOf("tooling", "open source", "premex")

    (plugins) {
        "gross" {
            displayName = "Generates a list of open source licenses you depend on"
            description =
                """Generates a list of open source licenses you depend on. Depends on the output of licensee from cashapp - https://github.com/cashapp/licensee.
                    |
                    |Can generate a static list or copy licenses to android assets. 
                    |
                    |As licensee supports KMM the plugin could support more platforms but current only supports android.
            """.trimMargin()
        }
    }
}


allprojects {
    tasks.withType<ValidatePlugins>().configureEach {
        enableStricterValidation.set(true)
    }
}