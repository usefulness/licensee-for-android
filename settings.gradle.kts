pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "app.cash.licensee") {
                // https://github.com/cashapp/licensee/issues/91
                useModule("app.cash.licensee:licensee-gradle-plugin:${requested.version}")
            }
        }
    }
}

plugins {
    id("com.gradle.enterprise") version "3.12.6"
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Gross"

includeBuild("gross-plugin")
includeBuild("ui")
includeBuild("core")
include(":app")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
 