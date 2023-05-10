# Gross

Gradle Open Source Software

## Plugin can be found at:
https://plugins.gradle.org/plugin/se.premex.gross

### Getting started
Gross uses the output of another plugin which figures out all your dependencies: https://github.com/cashapp/licensee

Add licensee and gross to your root project / module. We need both mavenCentral and gradle plugin portal for plugin resolution:

settings.gradle.kts
```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

build.gradle.kts
```kotlin
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("app.cash.licensee:licensee-gradle-plugin:1.7.0")
  }
}

plugins {
    id("app.cash.licensee")
    id("se.premex.gross") version "0.1.0"
}
```

### Gradle dsl:
gross {
    enableKotlinCodeGeneration.set(true)
    enableAndroidAssetGeneration.set(true)
}

## enableKotlinCodeGeneration
Generates a static list of open source assets in Gross.artifacts. 

## enableAndroidAssetGeneration
Saves licensee report as an android asset. Default name is 'artifacts.json' but can be configured 
using androidAssetFileName. Check out AssetLicenseParser for an example of how to consume the file.
