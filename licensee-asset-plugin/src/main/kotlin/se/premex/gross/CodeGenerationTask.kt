package se.premex.gross

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
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
    fun action() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val file = FileSpec.builder(packageName, "Artifact")
            .addType(licenseeTypesGenerator.spdxLicensesTypeSpec)
            .addType(licenseeTypesGenerator.scmTypeSpec)
            .addType(licenseeTypesGenerator.unknownLicensesTypeSpec)
            .addType(licenseeTypesGenerator.artifactTypeSpec)
            .build()
        file.writeTo(outputDirectory.asFile.get())

        val mutableListOf = MemberName("kotlin.collections", "mutableListOf")
        val mutableList = ClassName("kotlin.collections", "MutableList")
            .parameterizedBy(ClassName(packageName, licenseeTypesGenerator.artifactTypeSpec.name!!))

        val funSpec = FunSpec.builder("allArtifactsFun")
            .returns(licenseeTypesGenerator.artifactListType)
            .addStatement("val list: %T = %M()", mutableList, mutableListOf)

        val artifacts = LicenseParser().decode(inputFile.asFile.get().source().buffer())

        val artifactCodeGenerator = ArtifactCodeGenerator(
            packageName = packageName,
            spdxLicensesTypeSpec = licenseeTypesGenerator.spdxLicensesTypeSpec,
            scmTypeSpec = licenseeTypesGenerator.scmTypeSpec,
            unknownLicensesTypeSpec = licenseeTypesGenerator.unknownLicensesTypeSpec
        )
        artifacts.forEach {
            val codeBlockBuilder = CodeBlock.builder()
            codeBlockBuilder.addStatement(
                """list.%N(""", ClassName("kotlin.collections", "MutableList")
                    .member("add")
            )
            codeBlockBuilder.add(artifactCodeGenerator.artifactCodeBlock(artifact = it))
            codeBlockBuilder.addStatement("""|)""".trimMargin())
            funSpec.addCode(codeBlockBuilder.build())
        }

        val build = funSpec.addStatement("return list")
            .build()

        val grossType = TypeSpec.objectBuilder("Gross")
            .build()

        val allFiles = FileSpec.builder(packageName, "Artifacts")
            .addType(grossType)
            .addProperty(
                PropertySpec
                    .builder(
                        "allArtifacts", LIST.parameterizedBy(
                            ClassName(
                                packageName,
                                licenseeTypesGenerator.artifactTypeSpec.name!!
                            )
                        )
                    )
                    .initializer("mutableListOf()")
                    .build()
            )
            .addFunction(build)
            .build()

        allFiles.writeTo(outputDirectory.asFile.get())
    }
}