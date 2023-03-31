package se.premex.gross

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension
import org.gradle.kotlin.dsl.register
import java.util.Locale


class CopyLicenseeReportToAssetsPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val androidComponentsExtension =
            project.extensions.getByName("androidComponents") as ApplicationAndroidComponentsExtension

        val reportingExtension: ReportingExtension =
            project.extensions.getByType(ReportingExtension::class.java)

        androidComponentsExtension.onVariants { variant ->
            val capitalizedVariantName = variant.name.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }

            val copyArtifactsTask =
                project.tasks.register<AssetCopyTask>("copy${capitalizedVariantName}LicenseeReportToAssets") {
                    inputFile.set(reportingExtension.file("licensee/${variant.name}/artifacts.json"))
                }

            variant.sources.assets!!.addGeneratedSourceDirectory(
                copyArtifactsTask,
                AssetCopyTask::outputDirectory
            )

            val codeGenerationTask = project.tasks.register<CodeGenerationTask>("${capitalizedVariantName}LicenseeReportToKotlin") {
                inputFile.set(reportingExtension.file("licensee/${variant.name}/artifacts.json"))
            }

            variant.sources.java!!.addGeneratedSourceDirectory(
                codeGenerationTask,
                CodeGenerationTask::outputDirectory
            )

            codeGenerationTask.dependsOn("licensee${capitalizedVariantName}")

            copyArtifactsTask.dependsOn("licensee${capitalizedVariantName}")
        }
    }
}