/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All the built-in vanilla biome categories.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(BiomeCategory::class)
public object BiomeCategories {

    // @formatter:off
    @JvmField public val NONE: BiomeCategory = register("none")
    @JvmField public val TAIGA: BiomeCategory = register("taiga")
    @JvmField public val EXTREME_HILLS: BiomeCategory = register("extreme_hills")
    @JvmField public val JUNGLE: BiomeCategory = register("jungle")
    @JvmField public val MESA: BiomeCategory = register("mesa")
    @JvmField public val PLAINS: BiomeCategory = register("plains")
    @JvmField public val SAVANNA: BiomeCategory = register("savanna")
    @JvmField public val ICY: BiomeCategory = register("icy")
    @JvmField public val THE_END: BiomeCategory = register("the_end")
    @JvmField public val BEACH: BiomeCategory = register("beach")
    @JvmField public val FOREST: BiomeCategory = register("forest")
    @JvmField public val OCEAN: BiomeCategory = register("ocean")
    @JvmField public val DESERT: BiomeCategory = register("desert")
    @JvmField public val RIVER: BiomeCategory = register("river")
    @JvmField public val SWAMP: BiomeCategory = register("swamp")
    @JvmField public val MUSHROOM: BiomeCategory = register("mushroom")
    @JvmField public val NETHER: BiomeCategory = register("nether")
    @JvmField public val UNDERGROUND: BiomeCategory = register("underground")

    // @formatter:on
    private fun register(name: String): BiomeCategory {
        val key = Key.key(name)
        return Registries.register(Registries.BIOME_CATEGORIES, key, BiomeCategory.of(key))
    }
}
