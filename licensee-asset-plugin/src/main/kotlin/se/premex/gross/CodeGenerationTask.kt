package se.premex.gross

import com.squareup.kotlinpoet.ClassName
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
import se.premex.gross.core.Artifact
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

        val artifactListType =
            LIST.parameterizedBy(
                ClassName(
                    packageName,
                    licenseeTypesGenerator.artifactTypeSpec.name!!
                )
            )

        val funSpec = FunSpec.builder("allArtifactsFun")
            .returns(artifactListType)
            .addStatement("val list: %T = %M()", mutableList, mutableListOf)

        val artifacts = LicenseParser().decode(inputFile.asFile.get().source().buffer())

        artifacts.forEach {
            addFunSpec(
                it,
                funSpec,
                licenseeTypesGenerator.spdxLicensesTypeSpec,
                licenseeTypesGenerator.scmTypeSpec,
                licenseeTypesGenerator.unknownLicensesTypeSpec
            )
        }


        val build = funSpec.addStatement("return list")
            .build()

        val grossType = TypeSpec.objectBuilder("Gross")
            .build()

        val allFiles = FileSpec.builder(packageName, "Artifacts")
            .addType(grossType)
            .addProperty(
                PropertySpec
                    .builder("allArtifacts", artifactListType)
                    .initializer("mutableListOf()")
                    .build()
            )
            .addFunction(build)
            .build()

        allFiles.writeTo(outputDirectory.asFile.get())
    }

    private fun addFunSpec(
        artifact: Artifact,
        funSpec: FunSpec.Builder,
        spdxLicensesTypeSpec: TypeSpec,
        scmTypeSpec: TypeSpec,
        unknownLicensesTypeSpec: TypeSpec
    ) {
        val newArguments = mutableListOf<Any>()

        val statement = buildString {
            appendLine(
                """list.%N(Artifact(
                    |groupId = %S, 
                    |artifactId = %S, 
                    |version = %S, 
                    |name = %S,""".trimMargin()
            )
            newArguments.add(
                ClassName("kotlin.collections", "MutableList")
                    .member("add")
            )
            newArguments.add(artifact.groupId)
            newArguments.add(artifact.artifactId)
            newArguments.add(artifact.version)
            newArguments.add(artifact.name.toString())

            appendLine("""spdxLicenses = %M(""")
            newArguments.add(MemberName("kotlin.collections", "listOf"))
            artifact.spdxLicenses?.forEach {
                appendLine("""%T(identifier = %S, name = %S, url = %S)""".trimMargin())
                newArguments.add(ClassName(packageName, spdxLicensesTypeSpec.name!!))
                newArguments.add(it.identifier)
                newArguments.add(it.name)
                newArguments.add(it.url)
            }
            appendLine("""),""")

            appendLine("""scm = %T(%S), """.trimMargin())
            newArguments.add(ClassName(packageName, scmTypeSpec.name!!))
            newArguments.add(artifact.scm?.url.toString())

            appendLine("""unknownLicenses = %M(""")
            newArguments.add(MemberName("kotlin.collections", "listOf"))
            artifact.unknownLicenses?.forEach {
                appendLine("""%T(name = %S, url = %S)""".trimMargin())
                newArguments.add(ClassName(packageName, unknownLicensesTypeSpec.name!!))
                newArguments.add(it.name)
                newArguments.add(it.url)
            }
            appendLine("""),""")


            appendLine("""|))""".trimMargin())
        }
        funSpec.addStatement(statement, *newArguments.toTypedArray())
    }
}