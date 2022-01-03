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

import org.kryptonmc.api.entity.ArmorSlot

enum class EquipmentSlot(
    val type: Type,
    val index: Int,
    val displayName: String
) {

    MAIN_HAND(Type.HAND, 0, "mainhand"),
    OFF_HAND(Type.HAND, 1, "offhand"),
    FEET(Type.ARMOR, 0, "feet"),
    LEGS(Type.ARMOR, 1, "legs"),
    CHEST(Type.ARMOR, 2, "chest"),
    HEAD(Type.ARMOR, 3, "head");

    fun index(offset: Int): Int = offset + index

    enum class Type {

        HAND,
        ARMOR
    }

    companion object {

        private val BY_ARMOR_SLOT = mapOf(
            ArmorSlot.HELMET to HEAD,
            ArmorSlot.CHESTPLATE to CHEST,
            ArmorSlot.LEGGINGS to LEGS,
            ArmorSlot.BOOTS to FEET
        )

        @JvmStatic
        fun fromArmorSlot(slot: ArmorSlot): EquipmentSlot = BY_ARMOR_SLOT[slot]!!
    }
}
