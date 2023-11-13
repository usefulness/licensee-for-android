package io.github.usefulness.licensee.serialization

import kotlinx.serialization.Serializable

@Serializable
data class SpdxLicenses(
    val identifier: String,
    val name: String,
    val url: String,
)

@Serializable
data class Scm(val url: String)

@Serializable
data class UnknownLicenses(
    val name: String,
    val url: String,
)

@Serializable
data class Artifact(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String? = null,
    val spdxLicenses: List<SpdxLicenses> = emptyList(),
    val scm: Scm? = null,
    val unknownLicenses: List<UnknownLicenses> = emptyList(),
)
