package io.github.usefulness.licensee

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.usefulness.licensee.core.Artifact
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.buffer
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
public abstract class CodeGenerationTask : DefaultTask() {

    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFile
    public abstract val inputFile: RegularFileProperty

    @get:Input
    public abstract val packageName: Property<String>

    @TaskAction
    @ExperimentalSerializationApi
    public fun action() {
        val packageName = packageName.get()
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        FileSpec.builder(packageName, "Artifact")
            .addType(licenseeTypesGenerator.spdxLicensesTypeSpec)
            .addType(licenseeTypesGenerator.scmTypeSpec)
            .addType(licenseeTypesGenerator.unknownLicensesTypeSpec)
            .addType(licenseeTypesGenerator.artifactTypeSpec)
            .build().writeTo(outputDirectory.asFile.get())

        val artifacts = Json.decodeFromBufferedSource<List<Artifact>>(inputFile.asFile.get().source().buffer())

        val artifactCodeGenerator = ArtifactCodeGenerator(
            packageName = packageName,
            spdxLicensesTypeSpec = licenseeTypesGenerator.spdxLicensesTypeSpec,
            scmTypeSpec = licenseeTypesGenerator.scmTypeSpec,
            unknownLicensesTypeSpec = licenseeTypesGenerator.unknownLicensesTypeSpec,
        )

        val artifactList = CodeBlock.builder().apply {
            addStatement("%M(", MemberName("kotlin.collections", "listOf"))
            artifacts.forEach { artifact ->
                add(artifactCodeGenerator.artifactCodeBlock(artifact))
            }
            addStatement(")")
        }.build()

        val grossType = TypeSpec.objectBuilder("Licensee")
            .addProperty(
                PropertySpec.builder("artifacts", licenseeTypesGenerator.artifactListType)
                    .initializer(artifactList).build(),
            )
            .build()

        FileSpec.builder(packageName, "Licensee")
            .addType(grossType)
            .build().writeTo(outputDirectory.asFile.get())
    }
}
