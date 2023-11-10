package io.github.usefulness.licensee

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension

public class LicenseeForAndroidPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create("licenseeForAndroid", LicenseeForAndroidExtension::class.java)

        pluginManager.withPlugin("app.cash.licensee") {
            listOf(
                "com.android.application",
                "com.android.library",
                "com.android.dynamic-feature",
            )
                .forEach { pluginId ->
                    pluginManager.withPlugin(pluginId) {
                        configureAndroidPlugin(extension)
                    }
                }
        }
    }

    private fun Project.configureAndroidPlugin(extension: LicenseeForAndroidExtension) {
        val androidComponents = extensions.getByName("androidComponents") as AndroidComponentsExtension<*, *, *>
        val reportingExtension = extensions.getByType(ReportingExtension::class.java)

        androidComponents.onVariants { variant ->
            val source = extension.singularVariantName.getOrElse(variant.name)
            val sourceCapitalized = source.replaceFirstChar(Char::titlecase)

            val target = variant.name
            val targetCapitalized = target.replaceFirstChar(Char::titlecase)
            val licenseeTaskName = "licenseeAndroid$sourceCapitalized"

            val artifactsFile = reportingExtension.file("licensee/android$sourceCapitalized/artifacts.json")
            if (extension.enableAndroidAssetGeneration.get()) {
                val copyArtifactsTask = tasks.register(
                    "generate${sourceCapitalized}LicenseeAssetsFor$targetCapitalized",
                    AssetCopyTask::class.java,
                ) {
                    it.inputFile.set(artifactsFile)
                    it.targetFileName.set(extension.androidAssetFileName)
                }
                variant.sources.assets!!.addGeneratedSourceDirectory(
                    taskProvider = copyArtifactsTask,
                    wiredWith = AssetCopyTask::outputDirectory,
                )
                copyArtifactsTask.configure { it.dependsOn(licenseeTaskName) }
            }

            if (extension.enableKotlinCodeGeneration.get()) {
                val codeGenerationTask = tasks.register(
                    "generate${sourceCapitalized}LicenseeKotlinCodeFor$targetCapitalized",
                    CodeGenerationTask::class.java,
                ) {
                    it.inputFile.set(artifactsFile)
                }

                // Do NOT use `.kotlin` here: https://issuetracker.google.com/issues/268248348
                variant.sources.java!!.addGeneratedSourceDirectory(
                    taskProvider = codeGenerationTask,
                    wiredWith = CodeGenerationTask::outputDirectory,
                )

                codeGenerationTask.configure { it.dependsOn(licenseeTaskName) }
            }
        }
    }
}
