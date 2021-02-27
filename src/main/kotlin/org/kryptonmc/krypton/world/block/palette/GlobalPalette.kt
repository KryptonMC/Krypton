package org.kryptonmc.krypton.world.block.palette

import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.world.block.BlockState

//object GlobalPalette {
//
//    private val LOGGER = logger<GlobalPalette>()
//
//    val PALETTE: Map<NamespacedKey, RegistryBlock>
//
//    init {
//        LOGGER.debug("Loading block registry...")
//        val data = javaClass.classLoader.getResourceAsStream("blocks.json")!!
//            .reader(Charset.forName("UTF-8"))
//            .readText()
//        PALETTE = Json {}.decodeFromString(data)
//        LOGGER.debug("Block registry loaded!")
//    }
//}

// TODO: Fix global palette
object GlobalPalette {

    val PALETTE = mapOf(
        NamespacedKey(value = "air") to 0,
        NamespacedKey(value = "bedrock") to 33,
        NamespacedKey(value = "stone") to 1,
        NamespacedKey(value = "granite") to 2,
        NamespacedKey(value = "coal_ore") to 71,
        NamespacedKey(value = "dirt") to 10,
        NamespacedKey(value = "diorite") to 4,
        NamespacedKey(value = "iron_ore") to 70,
        NamespacedKey(value = "gold_ore") to 69,
        NamespacedKey(value = "redstone_ore") to 3886,
        NamespacedKey(value = "andesite") to 6,
        NamespacedKey(value = "diamond_ore") to 3354,
        NamespacedKey(value = "gravel") to 68,
        NamespacedKey(value = "lapis_ore") to 232,
        NamespacedKey(value = "cave_air") to 9670,
        NamespacedKey(value = "water") to 34,
        NamespacedKey(value = "seagrass") to 1345,
        NamespacedKey(value = "sand") to 66
    )
}

data class RegistryBlock(
    val states: List<RegistryBlockState>,
    val properties: Map<String, List<Any>> = emptyMap()
)

data class RegistryBlockState(
    val id: Int,
    val default: Boolean = false,
    val properties: Map<String, BlockState> = emptyMap()
)