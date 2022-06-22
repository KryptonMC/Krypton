/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla dimension types.
 */
@Catalogue(DimensionType::class)
public object DimensionTypes {

    // @formatter:off
    @JvmField
    public val OVERWORLD: DimensionType = get("overworld")
    @JvmField
    public val OVERWORLD_CAVES: DimensionType = get("overworld_caves")
    @JvmField
    public val THE_NETHER: DimensionType = get("the_nether")
    @JvmField
    public val THE_END: DimensionType = get("the_end")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): DimensionType = Registries.DIMENSION_TYPE[Key.key(name)]!!
}
