package io.github.usefulness.licensee

import org.gradle.api.Incubating
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

public open class LicenseeForAndroidExtension(objectFactory: ObjectFactory) {

    /**
     * Enables generating a static list of open source assets.
     */
    public val enableKotlinCodeGeneration: Property<Boolean> = objectFactory.property(default = false)

    /**
     * Generate kotlin code under given package
     * Default: `io.github.usefulness.licensee`
     */
    public val generatedPackageName: Property<String> = objectFactory.property(default = "io.github.usefulness.licensee")

    /**
     * Enables copying licensee report to asset(Android)/resource(JVM) directory, making it available under 'resourceFileName' name.
     */
    public val enableResourceGeneration: Property<Boolean> = objectFactory.property(default = false)

    /**
     * The name of the asset/resource file the licensee report gets copied to.
     */
    public val resourceFileName: Property<String> = objectFactory.property(default = "licensee_artifacts.json")

    /**
     * The name of the build variant that all variants will use to have always the same licensed, regardless of app variant.
     * Default: null - each build variant will get its unique report
     */
    @Incubating
    public val singularVariantName: Property<String> = objectFactory.property(default = null)

    /**
     * Automatically add `licensee-for-android-core` core artifact as an implementation dependency for the generated code.
     * The idea is to use the core artifact in a consumer project, and wire generated implementation via DI mechanism
     */
    public val automaticCoreDependencyManagement: Property<Boolean> = objectFactory.property(default = true)
}

internal inline fun <reified T> ObjectFactory.property(default: T? = null): Property<T> = property(T::class.java).apply {
    convention(default)
}

internal inline fun <reified T> ObjectFactory.setProperty(default: Set<T>? = null): SetProperty<T> = setProperty(T::class.java).apply {
    convention(default)
}
