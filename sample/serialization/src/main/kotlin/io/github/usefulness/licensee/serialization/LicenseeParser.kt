package io.github.usefulness.licensee.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.BufferedSource

object LicenseeParser {

    @OptIn(ExperimentalSerializationApi::class)
    fun decode(source: BufferedSource) = Json.decodeFromBufferedSource<List<Artifact>>(source)
}
