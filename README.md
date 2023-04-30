# Gross

Gradle Open Source Software

Gradle dsl:
gross {
    enableKotlinCodeGeneration.set(true)
    enableAndroidAssetGeneration.set(true)
}

## enableKotlinCodeGeneration
Generates a static list of open source assets in Gross.artifacts. 

## enableAndroidAssetGeneration
Saves licensee report as an android asset. Default name is 'artifacts.json' but can be configured 
using androidAssetFileName. Check out AssetLicenseParser for an example of how to consume the file.

