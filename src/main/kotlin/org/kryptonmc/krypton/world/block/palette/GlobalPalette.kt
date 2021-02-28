package org.kryptonmc.krypton.world.block.palette

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.registry.NamespacedKey

object GlobalPalette {

    val PALETTE: Map<NamespacedKey, RegistryBlock>

    init {
        val blocksText = javaClass.classLoader.getResourceAsStream("blocks.json")!!.reader(Charsets.UTF_8).readText()
        PALETTE = Json {}.decodeFromString(blocksText)
    }
}

@Serializable
data class RegistryBlock(
    val properties: Map<String, List<String>> = emptyMap(),
    val states: List<RegistryBlockState>
)

@Serializable
data class RegistryBlockState(
    val id: Int,
    val default: Boolean = false,
    val properties: Map<String, String> = emptyMap()
)