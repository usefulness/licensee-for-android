package se.premex.gross.core

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.BufferedSource

interface LicenseParser {
    fun decode(source: BufferedSource): List<Artifact> {
        return Json.decodeFromBufferedSource(source)
    }
}
