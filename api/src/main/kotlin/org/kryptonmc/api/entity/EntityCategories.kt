/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla mob categories.
 */
@Catalogue(EntityCategory::class)
public object EntityCategories {

    @JvmField public val MONSTER: EntityCategory = get("monster")
    @JvmField public val CREATURE: EntityCategory = get("creature")
    @JvmField public val AMBIENT: EntityCategory = get("ambient")
    @JvmField public val UNDERGROUND_WATER_CREATURE: EntityCategory = get("underground_water_creature")
    @JvmField public val WATER_CREATURE: EntityCategory = get("water_creature")
    @JvmField public val WATER_AMBIENT: EntityCategory = get("water_ambient")
    @JvmField public val MISC: EntityCategory = get("misc")

    @JvmStatic
    private fun get(name: String) = Registries.ENTITY_CATEGORIES[Key.key(name)]!!
}
