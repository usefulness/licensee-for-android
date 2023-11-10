package se.premex.gross

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension
import java.util.Locale

public class GrossPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("gross", GrossExtension::class.java)
        extension.enableKotlinCodeGeneration.convention(true)
        extension.enableAndroidAssetGeneration.convention(false)
        extension.androidAssetFileName.convention("artifacts.json")

        val reportingExtension: ReportingExtension =
            project.extensions.getByType(ReportingExtension::class.java)

        listOf(
            "com.android.application",
            "com.android.library",
            "com.android.dynamic-feature",
        )
            .forEach { pluginId ->
                project.pluginManager.withPlugin(pluginId) {
                    configureAndroidPlugin(project, extension, reportingExtension)
                }
            }
    }

    private fun configureAndroidPlugin(project: Project, extension: GrossExtension, reportingExtension: ReportingExtension) {
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
                    project.tasks.register("copy${capitalizedVariantName}LicenseeReportToAssets", AssetCopyTask::class.java) {
                        it.inputFile.set(artifactsFile)
                        it.targetFileName.set(extension.androidAssetFileName.get())
                    }
                variant.sources.assets!!.addGeneratedSourceDirectory(
                    copyArtifactsTask,
                    AssetCopyTask::outputDirectory,
                )
                copyArtifactsTask.configure { it.dependsOn("licensee$capitalizedVariantName") }
            }

            if (extension.enableKotlinCodeGeneration.get()) {
                val codeGenerationTask =
                    project.tasks.register("${capitalizedVariantName}LicenseeReportToKotlin", CodeGenerationTask::class.java) {
                        it.inputFile.set(artifactsFile)
                    }

                variant.sources.java!!.addGeneratedSourceDirectory(
                    codeGenerationTask,
                    CodeGenerationTask::outputDirectory,
                )

                codeGenerationTask.configure { it.dependsOn("licensee$capitalizedVariantName") }
            }
        }
    }
}
