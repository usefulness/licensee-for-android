package se.premex.gross

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LicenseeTypesGeneratorTest {

    private val packageName = "se.premex.gross"
    @Test
    fun testSpdxLicenses() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val spdxDefinition = """public data class SpdxLicenses(
  public val identifier: kotlin.String,
  public val name: kotlin.String,
  public val url: kotlin.String,
)
"""

        assertEquals(spdxDefinition, licenseeTypesGenerator.spdxLicensesTypeSpec.toString())
    }

    @Test
    fun testScm() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val spdxDefinition = """public data class Scm(
  public val url: kotlin.String,
)
"""

        assertEquals(spdxDefinition, licenseeTypesGenerator.scmTypeSpec.toString())

    }

    @Test
    fun testUnknownLicenses() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val unknownLicensesDefinition = """public data class UnknownLicenses(
  public val name: kotlin.String,
  public val url: kotlin.String,
)
"""

        assertEquals(unknownLicensesDefinition, licenseeTypesGenerator.unknownLicensesTypeSpec.toString())
    }

    @Test
    fun testArtifact() {
        val licenseeTypesGenerator = LicenseeTypesGenerator(packageName)

        val artifactDefinition = """public data class Artifact(
  public val groupId: kotlin.String,
  public val artifactId: kotlin.String,
  public val version: kotlin.String,
  public val name: kotlin.String?,
  public val spdxLicenses: kotlin.collections.List<se.premex.gross.SpdxLicenses>,
  public val scm: se.premex.gross.Scm?,
  public val unknownLicenses: kotlin.collections.List<se.premex.gross.UnknownLicenses>,
)
"""
        assertEquals(artifactDefinition, licenseeTypesGenerator.artifactTypeSpec.toString())
    }
}