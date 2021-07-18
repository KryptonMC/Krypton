package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.InternalRegistryKeys

object FluidTags {

    val WATER = get("water")
    val LAVA = get("lava")

    val TAGS = listOf(WATER, LAVA)

    private fun get(name: String) = KryptonTagManager.load(Key.key(name), InternalRegistryKeys.FLUID.location, "fluids", InternalRegistries.FLUID)
}
