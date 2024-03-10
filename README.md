# licensee-for-android

[![Build Status](https://github.com/usefulness/licensee-for-android/workflows/Build%20Project/badge.svg)](https://github.com/usefulness/licensee-for-android/actions)
[![Latest Version](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/io/github/usefulness/licensee-for-android/maven-metadata.xml?label=gradle)](https://plugins.gradle.org/plugin/io.github.usefulness.licensee-for-android)
![Maven Central](https://img.shields.io/maven-central/v/io.github.usefulness/licensee-for-android)


Turn raw [cashapp/licensee](https://github.com/cashapp/licensee) report into assets/Kotlin code that can be easily consumed from an Android app

### Features
- Access licenses report directly by a generated Kotlin code (accessible via static `io.github.usefulness.licensee.LicenseeForAndroid` object)
- Read _licensee_ report copied to projects assets directory in runtime (via `assetManager.open("licensee_artifacts.json")`)

![example](images/generated_code_dark.png#gh-dark-mode-only)
![example](images/generated_code_light.png#gh-light-mode-only)

### Installation

Available on:

- [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.usefulness.licensee-for-android)
- [Maven Central](https://mvnrepository.com/artifact/io.github.usefulness/licensee-for-android)

#### Apply the plugin

```groovy
plugins {
    id("app.cash.licensee") version "x.y.z"
    id("io.github.usefulness.licensee-for-android") version "{{version}}"
}
```

#### Configuration

Options can be configured in the `licenseeForAndroid` extension:

```groovy
licenseeForAndroid {
    enableKotlinCodeGeneration = false
    generatedPackageName = "io.github.usefulness.licensee"
    enableResourceGeneration = false
    resourceFileName = "licensee_artifacts.json"
    singularVariantName = null
    automaticCoreDependencyManagement = true
}
```

- `enableKotlinCodeGeneration` - Enables generating a static list of open source assets. 
- `generatedPackageName` - Generate kotlin code under given package 
- `enableResourceGeneration` - Enables copying _licensee_ report to asset(Android)/resource(JVM) directory, making it available under 'resourceFileName' name. 
- `resourceFileName` - The name of the asset/resource file the licensee report gets copied to. 
- `singularVariantName` - The name of the build variant which all build variants will use, to show always the same licenses. (i.e. `"paidRelease"`)
- `automaticCoreDependencyManagement` - Automatically add `licensee-for-android-core` core artifact as an implementation dependency for the generated code. The idea is to later use the same _core_ artifact within a consumer project, and wire generated implementation via DI mechanism

### Common recipes

#### Generate licensee information from any module

```groovy
plugins {
    id("com.android.application") // or `com.android.library`
    id("app.cash.licensee")
    id("io.github.usefulness.licensee-for-android")
}

licensee {
    allow("Apache-2.0")
}

licenseeForAndroid {
    enableKotlinCodeGeneration = true
    enableResourceGeneration = true
}
```
#### Generate Kotlin code in Kotlin-only module using licensee output from a different module

###### Gradle based approach
```groovy
plugins {
    id("org.jetbrains.kotlin.jvm") // or any other module type
    id("io.github.usefulness.licensee-for-android") apply(false) // do not generate licensee information for _this_ module
}

// Register custom, source-generating task, use `:app`'s `productionRelease` variant
def licenseeTarget = layout.buildDirectory.map { it.dir("generated/licensee") }
tasks.register("generateLicenseeKotlinCode", CodeGenerationTask) {
    it.inputFile.set(
            project(":app").tasks.named("licenseeAndroidProductionRelease")
                    .flatMap { it.outputDir.file("artifacts.json") }
    )
    it.outputDirectory.set(licenseeTarget)
    it.packageName.set("io.github.usefulness.licensee")
}

// Make sources discoverable in IDE (https://youtrack.jetbrains.com/issue/KT-45161)
sourceSets.named("main") {
    kotlin {
        srcDir(licenseeTarget)
    }
}

// Make them run on every compilation
tasks.named("compileKotlin") {
    dependsOn("generateLicenseeKotlinCode")
}
```

###### DI based approach

`app/build.gradle`:

```groovy
plugins {
    id("com.android.application") 
    id("app.cash.licensee")
    id("io.github.usefulness.licensee-for-android")
}
licenseeForAndroid {
    enableKotlinCodeGeneration = true
}
```
\+ provide `LicenseeForAndroid` object as `Licensee` interface using your DI framework

`consumer/build.gradle`:
```groovy
plugins {
    id("org.jetbrains.kotlin.jvm") // or any other module type
}

dependencies {
    implementation("io.github.usefulness:licensee-for-android-core:${{ version }")
}
```
\+ inject `Licensee` interface


### Credits
Huge thanks to [premex-ab/gross](https://github.com/premex-ab/gross) which this plugin forked from.   
