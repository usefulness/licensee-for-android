package io.github.usefulness.licensee

@Poko
public class Artifact(
    public val groupId: String,
    public val artifactId: String,
    public val version: String,
    public val name: String?,
    public val spdxLicenses: List<SpdxLicense>,
    public val scm: Scm?,
    public val unknownLicenses: List<UnknownLicense>,
)

@Poko
public class SpdxLicense(
    public val identifier: String,
    public val name: String,
    public val url: String,
)

@Poko
public class Scm(public val url: String)

@Poko
public class UnknownLicense(
    public val name: String?,
    public val url: String?,
)
