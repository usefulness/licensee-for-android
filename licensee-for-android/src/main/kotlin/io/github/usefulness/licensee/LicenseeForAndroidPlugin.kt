package io.github.usefulness.licensee

import com.android.build.api.variant.AndroidComponentsExtension
import io.github.usefulness.licensee.generated.LicenseeForAndroidBuildConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reporting.ReportingExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

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

            listOf("org.jetbrains.kotlin.jvm")
                .forEach { pluginId ->
                    pluginManager.withPlugin(pluginId) {
                        project.afterEvaluate { configureKotlinPlugin(extension) }
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

            val artifactsFile = reportingExtension.baseDirectory.file("licensee/android$sourceCapitalized/artifacts.json")

            if (extension.enableResourceGeneration.get()) {
                val copyArtifactsTask = tasks.register(
                    "generate${sourceCapitalized}LicenseeResourceFor$targetCapitalized",
                    LicenseeFileCopyTask::class.java,
                ) {
                    it.dependsOn(licenseeTaskName)
                    it.inputFile.set(artifactsFile)
                    it.targetFileName.set(extension.resourceFileName)
                }

                variant.sources.assets!!.addGeneratedSourceDirectory(
                    taskProvider = copyArtifactsTask,
                    wiredWith = LicenseeFileCopyTask::outputDirectory,
                )
            }

            if (extension.enableKotlinCodeGeneration.get()) {
                val codeGenerationTask = tasks.register(
                    "generate${sourceCapitalized}LicenseeKotlinCodeFor$targetCapitalized",
                    CodeGenerationTask::class.java,
                ) {
                    it.dependsOn(licenseeTaskName)
                    it.inputFile.set(artifactsFile)
                    it.packageName.set(extension.generatedPackageName)
                }

                // Do NOT use `.kotlin` here: https://issuetracker.google.com/issues/268248348
                variant.sources.java!!.addGeneratedSourceDirectory(
                    taskProvider = codeGenerationTask,
                    wiredWith = CodeGenerationTask::outputDirectory,
                )

                if (extension.automaticCoreDependencyManagement.get()) {
                    addCoreDependency()
                }
            }
        }
    }

    private fun Project.configureKotlinPlugin(extension: LicenseeForAndroidExtension) {
        val reportingExtension = extensions.getByType(ReportingExtension::class.java)
        val kotlinExtension = project.extensions.getByType(KotlinSourceSetContainer::class.java)
        val licenseeTaskName = "licensee"

        val artifactsFile = reportingExtension.baseDirectory.file("licensee/artifacts.json")
        if (extension.enableResourceGeneration.get()) {
            val kotlinSourceSet = kotlinExtension.sourceSets.named("main").get().resources
            val targetDirectory = layout.buildDirectory.map { it.dir("generated").dir("licenseeResources") }

            val copyArtifactsTask = tasks.register("generateLicenseeResource", LicenseeFileCopyTask::class.java) {
                it.dependsOn(licenseeTaskName)
                it.inputFile.set(artifactsFile)
                it.targetFileName.set(extension.resourceFileName)
                it.outputDirectory.set(targetDirectory)
            }
            kotlinSourceSet.srcDir(targetDirectory)

            tasks.named("processResources") {
                it.dependsOn(copyArtifactsTask)
            }
        }

        if (extension.enableKotlinCodeGeneration.get()) {
            val targetDirectory = layout.buildDirectory.map { it.dir("generated").dir("licensee") }
            val codeGenerationTask = tasks.register("generateLicenseeKotlinCode", CodeGenerationTask::class.java) {
                it.dependsOn(licenseeTaskName)
                it.inputFile.set(artifactsFile)
                it.packageName.set(extension.generatedPackageName)
                it.outputDirectory.set(targetDirectory)
            }

            kotlinExtension.sourceSets.named("main") {
                it.kotlin.srcDir(targetDirectory)
            }

            tasks.named("compileKotlin") {
                it.dependsOn(codeGenerationTask)
            }

            if (extension.automaticCoreDependencyManagement.get()) {
                addCoreDependency()
            }
        }
    }

    private fun Project.addCoreDependency() {
        dependencies.add(
            "implementation",
            "io.github.usefulness:licensee-for-android-core:${LicenseeForAndroidBuildConfig.VERSION}",
        )
    }
}
