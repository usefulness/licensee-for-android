package se.premex.gross.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.BufferedSource

class LicenseParser {

    @ExperimentalSerializationApi
    fun decode(source: BufferedSource): List<Artifact> {
        return Json.decodeFromBufferedSource(source)
    }
}
