package se.premex.gross

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension
import org.gradle.kotlin.dsl.register
import java.util.Locale

private enum class AndroidPlugin {
    Application,
    Library,
    DynamicFeature,
}

class GrossPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("gross", GrossExtension::class.java)
        extension.enableKotlinCodeGeneration.convention(true)
        extension.enableAndroidAssetGeneration.convention(false)
        extension.androidAssetFileName.convention("artifacts.json")

        val reportingExtension: ReportingExtension =
            project.extensions.getByType(ReportingExtension::class.java)

        val androidPlugin = if (project.plugins.hasPlugin("com.android.application")) {
            AndroidPlugin.Application
        } else if (project.plugins.hasPlugin("com.android.library")) {
            AndroidPlugin.Library
        } else if (project.plugins.hasPlugin("com.android.dynamic-feature")) {
            AndroidPlugin.DynamicFeature
        } else {
            null
        }

        if (androidPlugin != null) {
            configureAndroidPlugin(project, extension, reportingExtension)
        }
    }

    private fun configureAndroidPlugin(
        project: Project,
        extension: GrossExtension,
        reportingExtension: ReportingExtension
    ) {
        val androidComponentsExtension =
            project.extensions.getByName("androidComponents") as ApplicationAndroidComponentsExtension

        androidComponentsExtension.onVariants { variant ->
            val capitalizedVariantName = variant.name.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }

            val artifactsFile = reportingExtension.file("licensee/${variant.name}/artifacts.json")
            if (extension.enableAndroidAssetGeneration.get()) {
                val copyArtifactsTask =
                    project.tasks.register<AssetCopyTask>("copy${capitalizedVariantName}LicenseeReportToAssets") {
                        inputFile.set(artifactsFile)
                        targetFileName.set(extension.androidAssetFileName.get())
                    }
                variant.sources.assets!!.addGeneratedSourceDirectory(
                    copyArtifactsTask,
                    AssetCopyTask::outputDirectory
                )
                copyArtifactsTask.dependsOn("licensee$capitalizedVariantName")
            }

            if (extension.enableKotlinCodeGeneration.get()) {
                val codeGenerationTask =
                    project.tasks.register<CodeGenerationTask>("${capitalizedVariantName}LicenseeReportToKotlin") {
                        inputFile.set(artifactsFile)
                    }

                variant.sources.java!!.addGeneratedSourceDirectory(
                    codeGenerationTask,
                    CodeGenerationTask::outputDirectory
                )

                codeGenerationTask.dependsOn("licensee$capitalizedVariantName")
            }
        }
    }
}
