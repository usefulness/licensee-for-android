package io.github.usefulness.licensee

import example.generated.from.library.Licensee
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PluginLibaryIntegrationTest {

    @Test
    fun checkGeneratedCode() {
        assertThat(Licensee.artifacts).isNotEmpty()
    }
}
