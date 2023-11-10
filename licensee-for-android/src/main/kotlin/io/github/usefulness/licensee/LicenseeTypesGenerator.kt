package io.github.usefulness.licensee

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName

internal class LicenseeTypesGenerator(packageName: String) {

    val spdxLicensesTypeSpec: TypeSpec =
        TypeSpec.classBuilder("SpdxLicenses")
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("identifier", String::class)
                    .addParameter("name", String::class)
                    .addParameter("url", String::class)
                    .build(),
            ).addProperty(
                PropertySpec.builder("identifier", String::class)
                    .initializer("identifier")
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("name", String::class)
                    .initializer("name")
                    .build(),
            )
            .addProperty(
                PropertySpec.builder("url", String::class)
                    .initializer("url")
                    .build(),
            )
            .build()

    val scmTypeSpec = TypeSpec.classBuilder("Scm")
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("url", String::class)
                .build(),
        ).addProperty(
            PropertySpec.builder("url", String::class)
                .initializer("url")
                .build(),
        )
        .build()

    val unknownLicensesTypeSpec = TypeSpec.classBuilder("UnknownLicenses")
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("name", String::class)
                .addParameter("url", String::class)
                .build(),
        ).addProperty(
            PropertySpec.builder("name", String::class)
                .initializer("name")
                .build(),
        )
        .addProperty(
            PropertySpec.builder("url", String::class)
                .initializer("url")
                .build(),
        )
        .build()

    private val spdxListType =
        LIST.parameterizedBy(ClassName(packageName, spdxLicensesTypeSpec.name!!))

    private val scmType = ClassName(packageName, scmTypeSpec.name!!).copy(nullable = true)

    private val unknownLicensesType =
        LIST.parameterizedBy(ClassName(packageName, unknownLicensesTypeSpec.name!!))

    val artifactTypeSpec = TypeSpec.classBuilder("Artifact")
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("groupId", String::class)
                .addParameter("artifactId", String::class)
                .addParameter("version", String::class)
                .addParameter("name", String::class.asTypeName().copy(nullable = true))
                .addParameter("spdxLicenses", spdxListType)
                .addParameter("scm", scmType)
                .addParameter("unknownLicenses", unknownLicensesType)
                .build(),
        ).addProperty(
            PropertySpec.builder("groupId", String::class)
                .initializer("groupId")
                .build(),
        ).addProperty(
            PropertySpec.builder("artifactId", String::class)
                .initializer("artifactId")
                .build(),
        ).addProperty(
            PropertySpec.builder("version", String::class)
                .initializer("version")
                .build(),
        ).addProperty(
            PropertySpec.builder(
                "name",
                String::class.asTypeName().copy(nullable = true),
            )
                .initializer("name")
                .build(),
        ).addProperty(
            PropertySpec.builder("spdxLicenses", spdxListType)
                .initializer("spdxLicenses")
                .build(),
        ).addProperty(
            PropertySpec.builder("scm", scmType.copy(nullable = true))
                .initializer("scm")
                .build(),
        ).addProperty(
            PropertySpec.builder("unknownLicenses", unknownLicensesType)
                .initializer("unknownLicenses")
                .build(),
        )
        .build()

    val artifactListType = LIST.parameterizedBy(
        ClassName(
            packageName,
            artifactTypeSpec.name!!,
        ),
    )
}
