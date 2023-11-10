package se.premex.gross

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension

public class GrossPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("gross", GrossExtension::class.java)
        extension.enableKotlinCodeGeneration.convention(true)
        extension.enableAndroidAssetGeneration.convention(false)
        extension.androidAssetFileName.convention("artifacts.json")

        val reportingExtension: ReportingExtension =
            project.extensions.getByType(ReportingExtension::class.java)

        project.pluginManager.withPlugin("app.cash.licensee") {
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
    }

    private fun configureAndroidPlugin(project: Project, extension: GrossExtension, reportingExtension: ReportingExtension) {
        val androidComponentsExtension =
            project.extensions.getByName("androidComponents") as AndroidComponentsExtension<*, *, *>

        androidComponentsExtension.onVariants { variant ->
            val target = variant.name
            val targetCapitalized = target.replaceFirstChar(Char::titlecase)
            val licenseeTaskName = "licenseeAndroid$targetCapitalized"

            val artifactsFile = reportingExtension.file("licensee/android$targetCapitalized/artifacts.json")
            if (extension.enableAndroidAssetGeneration.get()) {
                val copyArtifactsTask =
                    project.tasks.register("copy${targetCapitalized}LicenseeReportToAssets", AssetCopyTask::class.java) {
                        it.inputFile.set(artifactsFile)
                        it.targetFileName.set(extension.androidAssetFileName.get())
                    }
                variant.sources.assets!!.addGeneratedSourceDirectory(
                    copyArtifactsTask,
                    AssetCopyTask::outputDirectory,
                )
                copyArtifactsTask.configure { it.dependsOn(licenseeTaskName) }
            }

            if (extension.enableKotlinCodeGeneration.get()) {
                val codeGenerationTask =
                    project.tasks.register("${target}LicenseeReportToKotlin", CodeGenerationTask::class.java) {
                        it.inputFile.set(artifactsFile)
                    }

                // Do NOT use `.kotlin` here: https://issuetracker.google.com/issues/268248348
                variant.sources.java!!.addGeneratedSourceDirectory(
                    codeGenerationTask,
                    CodeGenerationTask::outputDirectory,
                )

                codeGenerationTask.configure { it.dependsOn(licenseeTaskName) }
            }
        }
    }
}
