/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

object KryptonEntityCategories {

    @JvmField
    val MONSTER: KryptonEntityCategory = register("monster", 70, false, false, 128)
    @JvmField
    val CREATURE: KryptonEntityCategory = register("creature", 10, true, true, 128)
    @JvmField
    val AMBIENT: KryptonEntityCategory = register("ambient", 15, true, false, 128)
    @JvmField
    val UNDERGROUND_WATER_CREATURE: KryptonEntityCategory = register("underground_water_creature", 5, true, false, 128)
    @JvmField
    val WATER_CREATURE: KryptonEntityCategory = register("water_creature", 5, true, false, 128)
    @JvmField
    val WATER_AMBIENT: KryptonEntityCategory = register("water_ambient", 20, true, false, 64)
    @JvmField
    val MISC: KryptonEntityCategory = register("misc", -1, true, true, 128)

    @JvmStatic
    private fun register(name: String, max: Int, friendly: Boolean, persistent: Boolean, despawn: Int): KryptonEntityCategory =
        Registries.ENTITY_CATEGORIES.register(Key.key(name), KryptonEntityCategory(max, friendly, persistent, despawn, 32))
}
