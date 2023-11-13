package io.github.usefulness.licensee

import dev.drewhamilton.poko.Poko

@Poko
public class SpdxLicenses(
    public val identifier: String,
    public val name: String,
    public val url: String,
)

@Poko
public class Scm(public val url: String)

@Poko
public class UnknownLicenses(
    public val name: String,
    public val url: String,
)

@Poko
public class Artifact(
    public val groupId: String,
    public val artifactId: String,
    public val version: String,
    public val name: String?,
    public val spdxLicenses: List<SpdxLicenses>,
    public val scm: Scm?,
    public val unknownLicenses: List<UnknownLicenses>,
)
