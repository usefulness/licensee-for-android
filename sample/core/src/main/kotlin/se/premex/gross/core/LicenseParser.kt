package se.premex.gross.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.BufferedSource

interface LicenseParser {
    @ExperimentalSerializationApi
    fun decode(source: BufferedSource) = Json.decodeFromBufferedSource<List<Artifact>>(source)
}
