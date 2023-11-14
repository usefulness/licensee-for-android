package io.github.usefulness.licensee

class FakeLicensee(override val artifacts: List<Artifact>) : Licensee

fun stubArtifact(
    groupId: String = "com.example",
    artifactId: String = "foo-bar",
    version: String = "0.1.0-alpha04",
    name: String? = "Fixture Name of $groupId:$artifactId:$version",
    spdxLicenses: List<SpdxLicense> = listOf(stubSpdxLicense()),
    scm: Scm? = stubScm(),
    unknownLicenses: List<UnknownLicense> = listOf(stubUnknownLicense()),
) = Artifact(
    groupId = groupId,
    artifactId = artifactId,
    version = version,
    name = name,
    spdxLicenses = spdxLicenses,
    scm = scm,
    unknownLicenses = unknownLicenses,
)

fun stubSpdxLicense(
    identifier: String = "Apache-2.0",
    name: String = "Apache License 2.0",
    url: String = "https://www.apache.org/licenses/LICENSE-2.0",
) = SpdxLicense(
    identifier = identifier,
    name = name,
    url = url,
)

fun stubApacheLicense() = stubSpdxLicense(
    identifier = "Apache-2.0",
    name = "Apache License 2.0",
    url = "https://www.apache.org/licenses/LICENSE-2.0",
)

fun stubMitLicense() = stubSpdxLicense(
    identifier = "MIT",
    name = "MIT License",
    url = "https://opensource.org/licenses/MIT",
)

fun stubScm(url: String = "url") = Scm(url = url)

fun stubUnknownLicense(
    name: String = "Android Software Development Kit License",
    url: String = "https://developer.android.com/studio/terms.html",
) = UnknownLicense(
    name = name,
    url = url,
)
