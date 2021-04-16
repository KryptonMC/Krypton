package org.kryptonmc.krypton.world.block.palette

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.registry.json.RegistryBlock
import java.io.IOException

private val BLOCKS_TEXT = (Thread.currentThread().contextClassLoader.getResourceAsStream("registries/blocks.json")
    ?: throw IOException("registries/blocks.json not in classpath! Something has gone horribly wrong!"))
    .reader(Charsets.UTF_8)
    .readText()

private val PALETTE: Map<NamespacedKey, RegistryBlock> = Json.decodeFromString(BLOCKS_TEXT)

/**
 * The global block palette, backed by the block state registry
 *
 * @author Callum Seabrook
 */
object GlobalPalette : Map<NamespacedKey, RegistryBlock> by PALETTE {

    override fun get(key: NamespacedKey) = PALETTE.getValue(key)
}