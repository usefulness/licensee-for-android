package io.github.usefulness.licensee

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LicenseeTypesGeneratorTest {

    private val packageName = "io.github.usefulness.licensee"

    @Test
    fun testSpdxLicenses() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val spdxDefinition = """public data class SpdxLicenses(
  public val identifier: kotlin.String,
  public val name: kotlin.String,
  public val url: kotlin.String,
)
"""

        assertThat(licenseeTypesGenerator.spdxLicensesTypeSpec.toString()).isEqualTo(spdxDefinition)
    }

    @Test
    fun testScm() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val spdxDefinition = """public data class Scm(
  public val url: kotlin.String,
)
"""

        assertThat(licenseeTypesGenerator.scmTypeSpec.toString()).isEqualTo(spdxDefinition)
    }

    @Test
    fun testUnknownLicenses() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val unknownLicensesDefinition = """public data class UnknownLicenses(
  public val name: kotlin.String,
  public val url: kotlin.String,
)
"""

        assertThat(licenseeTypesGenerator.unknownLicensesTypeSpec.toString()).isEqualTo(unknownLicensesDefinition)
    }

    @Test
    fun testArtifact() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val artifactDefinition = """public data class Artifact(
  public val groupId: kotlin.String,
  public val artifactId: kotlin.String,
  public val version: kotlin.String,
  public val name: kotlin.String?,
  public val spdxLicenses: kotlin.collections.List<io.github.usefulness.licensee.SpdxLicenses>,
  public val scm: io.github.usefulness.licensee.Scm?,
  public val unknownLicenses: kotlin.collections.List<io.github.usefulness.licensee.UnknownLicenses>,
)
"""
        assertThat(licenseeTypesGenerator.artifactTypeSpec.toString()).isEqualTo(artifactDefinition)
    }
}
