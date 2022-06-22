/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import org.kryptonmc.api.Krypton
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Tag::class)
public object FluidTags {

    // @formatter:off
    @JvmField
    public val WATER: Tag<Fluid> = get("water")
    @JvmField
    public val LAVA: Tag<Fluid> = get("lava")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Tag<Fluid> = Krypton.tagManager[TagTypes.FLUIDS, "minecraft:$key"]!!
}
