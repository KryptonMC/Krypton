package org.kryptonmc.krypton.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.biome.BiomeFactory
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonBiomeFactory : BiomeFactory {

    override fun get(name: String) = InternalRegistries.BIOME[Key.key(name)]!!
}
