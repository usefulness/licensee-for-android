package io.github.usefulness.licensee

import io.github.usefulness.licensee.core.Artifact
import io.github.usefulness.licensee.core.Scm
import io.github.usefulness.licensee.core.SpdxLicenses
import io.github.usefulness.licensee.core.UnknownLicenses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ArtifactGeneratorTest {

    @Test
    fun `test all nullables are null`() {
        val artifactCodeBlock = ArtifactCodeGenerator.artifactCodeBlock(
            Artifact(
                groupId = "testGroup",
                artifactId = "testArtifact",
                version = "testVersion",
                name = null,
                spdxLicenses = null,
                scm = null,
                unknownLicenses = null,
            ),
        )

        // language
        val expected = """
            io.github.usefulness.licensee.Artifact(
              groupId = "testGroup",
              artifactId = "testArtifact",
              version = "testVersion",
              name = null,
              spdxLicenses = kotlin.collections.emptyList(),
              scm = null,
              unknownLicenses = kotlin.collections.emptyList(),
            ),

        """.trimIndent()
        assertThat(artifactCodeBlock).asString().isEqualToIgnoringWhitespace(expected)
    }

    @Test
    fun `test all lists are empty`() {
        val artifactCodeBlock = ArtifactCodeGenerator.artifactCodeBlock(
            Artifact(
                groupId = "testGroup",
                artifactId = "testArtifact",
                version = "testVersion",
                name = "testName",
                spdxLicenses = listOf(),
                scm = Scm("testUrl"),
                unknownLicenses = listOf(),
            ),
        )
        val expected = """
            io.github.usefulness.licensee.Artifact(
              groupId = "testGroup",
              artifactId = "testArtifact",
              version = "testVersion",
              name = "testName",
              spdxLicenses = kotlin.collections.emptyList(),
              scm = io.github.usefulness.licensee.Scm(url = "testUrl"),
              unknownLicenses = kotlin.collections.emptyList(),
            ),

        """.trimIndent()
        assertThat(artifactCodeBlock).asString().isEqualToIgnoringWhitespace(expected)
    }

    @Test
    fun `test all lists have items`() {
        val artifactCodeBlock = ArtifactCodeGenerator.artifactCodeBlock(
            Artifact(
                groupId = "testGroup",
                artifactId = "testArtifact",
                version = "testVersion",
                name = "testName",
                spdxLicenses = listOf(
                    SpdxLicenses("spdxId1", "spdxName1", "spdxUrl1"),
                    SpdxLicenses("spdxId2", "spdxName2", "spdxUrl2"),
                ),
                scm = Scm("testUrl"),
                unknownLicenses = listOf(
                    UnknownLicenses("unknown1", "unknown1"),
                    UnknownLicenses("unknown2", "unknown2"),
                ),
            ),
        )
        val expected = """
            io.github.usefulness.licensee.Artifact(
                groupId = "testGroup",
                artifactId = "testArtifact",
                version = "testVersion",
                name = "testName",
                spdxLicenses = kotlin.collections.listOf(
                  io.github.usefulness.licensee.SpdxLicense(
                    identifier = "spdxId1",
                    name = "spdxName1",
                    url = "spdxUrl1",
                  ),
                  io.github.usefulness.licensee.SpdxLicense(
                    identifier = "spdxId2",
                    name = "spdxName2",
                    url = "spdxUrl2",
                  ),
                ),
                scm = io.github.usefulness.licensee.Scm(url = "testUrl"),
                unknownLicenses = kotlin.collections.listOf(
                  io.github.usefulness.licensee.UnknownLicense(
                    name = "unknown1",
                    url = "unknown1",
                  ),
                  io.github.usefulness.licensee.UnknownLicense(
                    name = "unknown2",
                    url = "unknown2",
                  ),
                ),
              ),

        """.trimIndent()
        assertThat(artifactCodeBlock).asString().isEqualToIgnoringWhitespace(expected)
    }
}
