/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Fluid::class)
public object Fluids {

    // @formatter:off
    @JvmField
    public val EMPTY: RegistryReference<Fluid> = of("empty")
    @JvmField
    public val FLOWING_WATER: RegistryReference<Fluid> = of("flowing_water")
    @JvmField
    public val WATER: RegistryReference<Fluid> = of("water")
    @JvmField
    public val FLOWING_LAVA: RegistryReference<Fluid> = of("flowing_lava")
    @JvmField
    public val LAVA: RegistryReference<Fluid> = of("lava")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<Fluid> = RegistryReference.of(Registries.FLUID, Key.key(name))
}
