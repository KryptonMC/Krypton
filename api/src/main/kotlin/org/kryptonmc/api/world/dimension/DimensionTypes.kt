/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.registry.Registries
import java.util.OptionalLong

object DimensionTypes {

    // @formatter:off
    @JvmField val INFINIBURN_OVERWORLD = key("infiniburn_overworld")
    @JvmField val INFINIBURN_NETHER = key("infiniburn_nether")
    @JvmField val INFINIBURN_END = key("infiniburn_end")

    @JvmField val OVERWORLD_KEY = key("overworld")
    @JvmField val THE_NETHER_KEY = key("the_nether")
    @JvmField val THE_END_KEY = key("the_end")
    // @formatter:on

    /**
     * The built-in overworld dimension type.
     */
    @JvmField
    val OVERWORLD = register("overworld", DimensionType(
        OVERWORLD_KEY,
        false,
        true,
        false,
        true,
        false,
        true,
        true,
        false,
        0F,
        null,
        INFINIBURN_OVERWORLD,
        OVERWORLD_KEY,
        0,
        256,
        256,
        1.0
    ))

    /**
     * The built-in overworld caves dimension type.
     */
    @JvmField
    val OVERWORLD_CAVES = register("overworld_caves", DimensionType(
        OVERWORLD_KEY,
        false,
        true,
        false,
        true,
        true,
        true,
        true,
        false,
        0F,
        null,
        INFINIBURN_OVERWORLD,
        OVERWORLD_KEY,
        0,
        256,
        256,
        1.0
    ))
    @JvmField
    val THE_NETHER = register("the_nether", DimensionType(
        THE_NETHER_KEY,
        true,
        false,
        true,
        false,
        true,
        false,
        false,
        true,
        0.1F,
        18000L,
        INFINIBURN_NETHER,
        THE_NETHER_KEY,
        0,
        256,
        128,
        8.0
    ))
    @JvmField
    val THE_END = register("the_end", DimensionType(
        THE_END_KEY,
        false,
        false,
        false,
        false,
        false,
        true,
        false,
        false,
        0F,
        6000L,
        INFINIBURN_END,
        THE_END_KEY,
        0,
        256,
        256,
        1.0
    ))

    private fun register(key: String, value: DimensionType) = Registries.register(Registries.DIMENSION_TYPE, key, value)
}
