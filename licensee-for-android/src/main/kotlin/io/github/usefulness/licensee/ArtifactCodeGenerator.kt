package io.github.usefulness.licensee

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.withIndent
import io.github.usefulness.licensee.serialization.Artifact

internal object ArtifactCodeGenerator {

    val entrypointType = coreClassName("Licensee")
    val artifactListType = LIST.parameterizedBy(coreClassName("Artifact"))

    fun artifactCodeBlock(artifact: Artifact) = CodeBlock.builder()
        .withIndent {
            addStatement("%T(", ARTIFACT_KCLASS_NAME)
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
                            addStatement("%T(", SPDX_LICENSE_KCLASS_NAME)
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
                    addStatement("scm = %T(url = %S),", SCM_KCLASS_NAME, artifact.scm.url)
                }

                if (artifact.unknownLicenses.isNullOrEmpty()) {
                    addStatement("unknownLicenses = %M(),", MemberName("kotlin.collections", "emptyList"))
                } else {
                    addStatement("unknownLicenses = %M(", MemberName("kotlin.collections", "listOf"))
                    withIndent {
                        artifact.unknownLicenses.forEach { license ->
                            addStatement("%T(", UNKNOWN_LICENSE_KCLASS_NAME)
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

    private fun coreClassName(name: String) = ClassName("io.github.usefulness.licensee", name)

    private val SPDX_LICENSE_KCLASS_NAME = coreClassName("SpdxLicense")
    private val SCM_KCLASS_NAME = coreClassName("Scm")
    private val UNKNOWN_LICENSE_KCLASS_NAME = coreClassName("UnknownLicense")
    private val ARTIFACT_KCLASS_NAME = coreClassName("Artifact")
}
