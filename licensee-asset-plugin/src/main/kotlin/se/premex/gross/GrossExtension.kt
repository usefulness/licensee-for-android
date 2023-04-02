package se.premex.gross

import org.gradle.api.provider.Property

interface GrossExtension {
    /**
     *
     */
    val enableKotlinCodeGeneration: Property<Boolean>

    /**
     * Enable asset generation. Will copy licensee report to
     * android asset directory making it available as 'androidAssetFileName'
     */
    val enableAndroidAssetGeneration: Property<Boolean>

    /**
     * The name of the asset file the licensee report gets copied to.
     * Default: "artifacts.json"
     */
    val androidAssetFileName: Property<String>
}