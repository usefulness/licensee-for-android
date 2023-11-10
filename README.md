# lincesee-for-android
___

Turn raw [cashapp/licensee](https://github.com/cashapp/licensee) report into assets/Kotlin code that can be easily consumed from an Android app

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


### Features
- Access licenses report directly by a generated Kotlin code (accessible via static `io.github.usefulness.licensee.Licensee` object) 
- Read _licensee_ report copied to projects assets directory in runtime (via `assetManager.open("licensee_artifacts.json")`)

### Configuration

Options can be configured in the `licenseeForAndroid` extension:

```groovy
licenseeForAndroid {
  enableKotlinCodeGeneration = false
  enableAndroidAssetGeneration = true
  androidAssetFileName = "licensee_artifacts.json"
  singularVariantName = null
}
```

- `enableKotlinCodeGeneration` - Generates a static list of open source assets 
- `enableAndroidAssetGeneration` - Enable asset generation. Will copy licensee report to android asset directory making it available as `androidAssetFileName` 
- `androidAssetFileName` - The name of the asset file the licensee report gets copied to. 
- `singularVariantName` - The name of the build variant that all variants will use to have always the same licensed, regardless of app variant. (i.e. "productionRelease")


### Credits
Huge thanks to [premex-ab/gross](https://github.com/premex-ab/gross) which this plugin forked from.   
