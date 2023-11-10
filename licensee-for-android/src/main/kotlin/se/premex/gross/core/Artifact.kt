package se.premex.gross.core

import kotlinx.serialization.Serializable

@Serializable
internal data class SpdxLicenses(
    val identifier: String,
    val name: String,
    val url: String,
)

@Serializable
internal data class Scm(val url: String)

@Serializable
internal data class UnknownLicenses(
    val name: String,
    val url: String,
)

@Serializable
internal data class Artifact(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String? = null,
    val spdxLicenses: List<SpdxLicenses>? = null,
    val scm: Scm? = null,
    val unknownLicenses: List<UnknownLicenses>? = null,
)
