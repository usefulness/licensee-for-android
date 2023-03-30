package se.premex.gross.oss

import kotlinx.coroutines.test.runTest
import kotlin.test.Test

private const val artifactsSmall = """[
    {
        "groupId": "androidx.activity",
        "artifactId": "activity",
        "version": "1.7.0",
        "name": "Activity",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    }
]"""

private const val artifactsMedium = """[
    {
        "groupId": "androidx.activity",
        "artifactId": "activity",
        "version": "1.7.0",
        "name": "Activity",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    },
    {
        "groupId": "androidx.activity",
        "artifactId": "activity-compose",
        "version": "1.7.0",
        "name": "Activity Compose",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    },
    {
        "groupId": "androidx.activity",
        "artifactId": "activity-ktx",
        "version": "1.7.0",
        "name": "Activity Kotlin Extensions",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    }
]"""

class LicenseParserTest {
    @Test
    fun testSmall() = runTest {
        val licenseParser = LicenseParser()
        licenseParser.decodeString(artifactsSmall)
        licenseParser.decodeString(artifactsMedium)
    }
    @Test
    fun testMedium() = runTest {
        val licenseParser = LicenseParser()
        licenseParser.decodeString(artifactsSmall)
        licenseParser.decodeString(artifactsMedium)
    }
}