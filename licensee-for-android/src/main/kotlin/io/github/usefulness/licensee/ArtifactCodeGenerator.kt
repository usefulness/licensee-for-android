package io.github.usefulness.licensee

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.withIndent
import io.github.usefulness.licensee.core.Artifact

internal class ArtifactCodeGenerator(
    private val packageName: String,
    private val spdxLicensesTypeSpec: TypeSpec,
    private val scmTypeSpec: TypeSpec,
    private val unknownLicensesTypeSpec: TypeSpec,
) {

    fun artifactCodeBlock(artifact: Artifact) = CodeBlock.builder()
        .withIndent {
            addStatement("Artifact(")
            withIndent {
                addStatement("groupId = %S,", artifact.groupId)
                addStatement("artifactId = %S,", artifact.artifactId)
                addStatement("version = %S,", artifact.version)
                addStatement("name = %S,", artifact.name)

                if (artifact.spdxLicenses.isNullOrEmpty()) {
                    addStatement("spdxLicenses = %M(),", MemberName("kotlin.collections", "emptyList"))
                } else {
                    addStatement("spdxLicenses = %M(", MemberName("kotlin.collections", "listOf"))
                    withIndent {
                        artifact.spdxLicenses.forEach { license ->
                            addStatement("%T(", ClassName(packageName, spdxLicensesTypeSpec.name!!))
                            withIndent {
                                addStatement("identifier = %S,", license.identifier)
                                addStatement("name = %S,", license.name)
                                addStatement("url = %S,", license.url)
                            }
                            addStatement("),")
                        }
                    }
                    addStatement("),")
                }

                if (artifact.scm == null) {
                    addStatement("scm = null,")
                } else {
                    addStatement("scm = %T(url = %S),", ClassName(packageName, scmTypeSpec.name!!), artifact.scm.url)
                }

                if (artifact.unknownLicenses.isNullOrEmpty()) {
                    addStatement("unknownLicenses = %M(),", MemberName("kotlin.collections", "emptyList"))
                } else {
                    addStatement("unknownLicenses = %M(", MemberName("kotlin.collections", "listOf"))
                    withIndent {
                        artifact.unknownLicenses.forEach { license ->
                            addStatement("%T(", ClassName(packageName, unknownLicensesTypeSpec.name!!))
                            withIndent {
                                addStatement("name = %S,", license.name)
                                addStatement("url = %S,", license.url)
                            }
                            addStatement("),")
                        }
                    }
                    addStatement("),")
                }
            }
            addStatement("),")
        }
        .build()
}
