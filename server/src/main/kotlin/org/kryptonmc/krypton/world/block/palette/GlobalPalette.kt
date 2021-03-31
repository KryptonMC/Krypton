package org.kryptonmc.krypton.world.block.palette

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.api.registry.NamespacedKey
import java.io.IOException

// these allow us to use delegated inheritance in GlobalPalette, making it easier to access the values
private val BLOCKS_TEXT = (Thread.currentThread().contextClassLoader.getResourceAsStream("registries/blocks.json")
    ?: throw IOException("registries/blocks.json not in classpath! Something has gone horribly wrong!"))
    .reader(Charsets.UTF_8)
    .readText()

private val PALETTE: Map<NamespacedKey, RegistryBlock> = Json.decodeFromString(BLOCKS_TEXT)

object GlobalPalette : Map<NamespacedKey, RegistryBlock> by PALETTE {

    override fun get(key: NamespacedKey) = PALETTE.getValue(key)
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