package io.github.usefulness.licensee

import org.gradle.api.Incubating
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

public open class LicenseeForAndroidExtension(objectFactory: ObjectFactory) {

    /**
     * Generates a static list of open source assets
     */
    public val enableKotlinCodeGeneration: Property<Boolean> = objectFactory.property(default = false)

    /**
     * Enable asset generation. Will copy licensee report to
     * android asset directory making it available as 'androidAssetFileName'
     */
    public val enableAndroidAssetGeneration: Property<Boolean> = objectFactory.property(default = true)

    /**
     * The name of the asset file the licensee report gets copied to.
     */
    public val androidAssetFileName: Property<String> = objectFactory.property(default = "licensee_artifacts.json")

    /**
     * The name of the build variant that all variants will use to have always the same licensed, regardless of app variant.
     * Default: null - each build variant will get its unique report
     */
    @Incubating
    public val singularVariantName: Property<String> = objectFactory.property(default = null)
}

internal inline fun <reified T> ObjectFactory.property(default: T? = null): Property<T> = property(T::class.java).apply {
    convention(default)
}
