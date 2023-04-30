package se.premex.gross

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.ExperimentalSerializationApi
import okio.buffer
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import se.premex.gross.core.LicenseParser

abstract class CodeGenerationTask : DefaultTask() {
    private val packageName = "se.premex.gross"

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @TaskAction
    @ExperimentalSerializationApi
    fun action() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        FileSpec.builder(packageName, "Artifact")
            .addType(licenseeTypesGenerator.spdxLicensesTypeSpec)
            .addType(licenseeTypesGenerator.scmTypeSpec)
            .addType(licenseeTypesGenerator.unknownLicensesTypeSpec)
            .addType(licenseeTypesGenerator.artifactTypeSpec)
            .build().writeTo(outputDirectory.asFile.get())

        val artifacts = LicenseParser().decode(inputFile.asFile.get().source().buffer())

        val artifactCodeGenerator = ArtifactCodeGenerator(
            packageName = packageName,
            spdxLicensesTypeSpec = licenseeTypesGenerator.spdxLicensesTypeSpec,
            scmTypeSpec = licenseeTypesGenerator.scmTypeSpec,
            unknownLicensesTypeSpec = licenseeTypesGenerator.unknownLicensesTypeSpec
        )

        val artifactList = CodeBlock.builder().apply {
            addStatement("%M(", MemberName("kotlin.collections", "listOf"))
            artifacts.forEach { artifact ->
                add(artifactCodeGenerator.artifactCodeBlock(artifact))
                addStatement(",")
            }
            addStatement(")")
        }.build()

        val grossType = TypeSpec.objectBuilder("Gross")
            .addProperty(
                PropertySpec.builder("artifacts", licenseeTypesGenerator.artifactListType)
                    .initializer(artifactList).build()
            )
            .build()

        FileSpec.builder(packageName, "Gross")
            .addType(grossType)
            .build().writeTo(outputDirectory.asFile.get())
    }
}
