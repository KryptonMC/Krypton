/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.resource.ResourceKeys

/**
 * This file is auto-generated. Do not edit this manually!
 */
public object FluidTags {

    // @formatter:off
    @JvmField
    public val WATER: TagKey<Fluid> = get("water")
    @JvmField
    public val LAVA: TagKey<Fluid> = get("lava")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): TagKey<Fluid> = TagKey.of(ResourceKeys.FLUID, Key.key(key))
}
