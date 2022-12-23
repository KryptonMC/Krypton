/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla mob categories.
 */
@Catalogue(EntityCategory::class)
public object EntityCategories {

    @JvmField
    public val MONSTER: RegistryReference<EntityCategory> = of("monster")
    @JvmField
    public val CREATURE: RegistryReference<EntityCategory> = of("creature")
    @JvmField
    public val AMBIENT: RegistryReference<EntityCategory> = of("ambient")
    @JvmField
    public val UNDERGROUND_WATER_CREATURE: RegistryReference<EntityCategory> = of("underground_water_creature")
    @JvmField
    public val WATER_CREATURE: RegistryReference<EntityCategory> = of("water_creature")
    @JvmField
    public val WATER_AMBIENT: RegistryReference<EntityCategory> = of("water_ambient")
    @JvmField
    public val MISC: RegistryReference<EntityCategory> = of("misc")

    @JvmStatic
    private fun of(name: String): RegistryReference<EntityCategory> = RegistryReference.of(Registries.ENTITY_CATEGORIES, Key.key(name))
}
