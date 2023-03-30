plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
}
buildscript {
    dependencies {
        classpath(libs.com.android.tools.build.gradle)
    }
}

dependencies {
    implementation(libs.com.android.tools.build.gradle)
    implementation(libs.org.jetbrains.kotlin.kotlin.gradle.plugin)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
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
