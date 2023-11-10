package io.github.usefulness.licensee

import io.github.usefulness.licensee.core.Artifact
import io.github.usefulness.licensee.core.Scm
import io.github.usefulness.licensee.core.SpdxLicenses
import io.github.usefulness.licensee.core.UnknownLicenses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ArtifactGeneratorTest {

    private val packageName = "se.premex.gross"

    private val licenseeTypesGenerator =
        LicenseeTypesGenerator(packageName = packageName)

    private val artifactCodeGenerator =
        ArtifactCodeGenerator(
            packageName = packageName,
            licenseeTypesGenerator.unknownLicensesTypeSpec,
            licenseeTypesGenerator.spdxLicensesTypeSpec,
            licenseeTypesGenerator.scmTypeSpec,
        )

    @Test
    fun `test all nullables are null`() {
        val artifactCodeBlock = artifactCodeGenerator.artifactCodeBlock(
            Artifact(
                "testGroup",
                "testArtifact",
                "testVersion",
                null,
                null,
                null,
                null,
            ),
        )
        assertThat(artifactCodeBlock).asString().isEqualTo(
            """Artifact(
    groupId = "testGroup",
    artifactId = "testArtifact",
    version = "testVersion",
    name = null,
    spdxLicenses = kotlin.collections.listOf(
    ),
    scm = null, 
    unknownLicenses = kotlin.collections.listOf(
    ),
    )

""",
        )
    }

    @Test
    fun `test all lists are empty`() {
        val artifactCodeBlock = artifactCodeGenerator.artifactCodeBlock(
            Artifact(
                "testGroup",
                "testArtifact",
                "testVersion",
                "testName",
                listOf(),
                Scm("testUrl"),
                listOf(),
            ),
        )
        assertThat(artifactCodeBlock).asString().isEqualTo(
            """Artifact(
    groupId = "testGroup",
    artifactId = "testArtifact",
    version = "testVersion",
    name = "testName",
    spdxLicenses = kotlin.collections.listOf(
    ),
    scm = se.premex.gross.SpdxLicenses("testUrl"), 
    unknownLicenses = kotlin.collections.listOf(
    ),
    )

""",
        )
    }

    @Test
    fun `test all lists have items`() {
        val artifactCodeBlock = artifactCodeGenerator.artifactCodeBlock(
            Artifact(
                "testGroup",
                "testArtifact",
                "testVersion",
                "testName",
                listOf(
                    SpdxLicenses("spdxId1", "spdxName1", "spdxUrl1"),
                    SpdxLicenses("spdxId2", "spdxName2", "spdxUrl2"),
                ),
                Scm("testUrl"),
                listOf(
                    UnknownLicenses("unknown1", "unknown1"),
                    UnknownLicenses("unknown2", "unknown2"),
                ),
            ),
        )
        assertThat(artifactCodeBlock).asString().isEqualTo(
            """Artifact(
    groupId = "testGroup",
    artifactId = "testArtifact",
    version = "testVersion",
    name = "testName",
    spdxLicenses = kotlin.collections.listOf(
    se.premex.gross.UnknownLicenses(identifier = "spdxId1", name = "spdxName1", url = "spdxUrl1")
    se.premex.gross.UnknownLicenses(identifier = "spdxId2", name = "spdxName2", url = "spdxUrl2")
    ),
    scm = se.premex.gross.SpdxLicenses("testUrl"), 
    unknownLicenses = kotlin.collections.listOf(
    se.premex.gross.Scm(name = "unknown1", url = "unknown1")
    se.premex.gross.Scm(name = "unknown2", url = "unknown2")
    ),
    )

""",
        )
    }
}
