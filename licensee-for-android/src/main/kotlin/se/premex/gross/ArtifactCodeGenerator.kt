package se.premex.gross

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeSpec
import se.premex.gross.core.Artifact

internal class ArtifactCodeGenerator(
    private val packageName: String,
    private val spdxLicensesTypeSpec: TypeSpec,
    private val scmTypeSpec: TypeSpec,
    private val unknownLicensesTypeSpec: TypeSpec,
) {

    @Suppress("SpreadOperator")
    fun artifactCodeBlock(artifact: Artifact): CodeBlock {
        val arguments = mutableListOf<Any>()

        val statement = buildString {
            appendLine(
                """Artifact(
                    |groupId = %S,
                    |artifactId = %S,
                    |version = %S,
                """.trimMargin(),
            )
            arguments.add(artifact.groupId)
            arguments.add(artifact.artifactId)
            arguments.add(artifact.version)
            if (artifact.name != null) {
                appendLine("""name = %S,""")
                arguments.add(artifact.name)
            } else {
                appendLine("""name = null,""")
            }

            appendLine("""spdxLicenses = %M(""")
            arguments.add(MemberName("kotlin.collections", "listOf"))
            artifact.spdxLicenses?.forEach {
                appendLine("""%T(identifier = %S, name = %S, url = %S)""".trimMargin())
                arguments.add(ClassName(packageName, spdxLicensesTypeSpec.name!!))
                arguments.add(it.identifier)
                arguments.add(it.name)
                arguments.add(it.url)
            }
            appendLine("""),""")

            if (artifact.scm != null) {
                appendLine("""scm = %T(%S), """.trimMargin())
                arguments.add(ClassName(packageName, scmTypeSpec.name!!))
                arguments.add(artifact.scm.url)
            } else {
                appendLine("""scm = null, """.trimMargin())
            }

            appendLine("""unknownLicenses = %M(""")
            arguments.add(MemberName("kotlin.collections", "listOf"))
            artifact.unknownLicenses?.forEach {
                appendLine("""%T(name = %S, url = %S)""".trimMargin())
                arguments.add(ClassName(packageName, unknownLicensesTypeSpec.name!!))
                arguments.add(it.name)
                arguments.add(it.url)
            }
            appendLine("""),""")

            appendLine("""|)""".trimMargin())
        }
        return CodeBlock.builder().addStatement(statement, *arguments.toTypedArray()).build()
    }
}
