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
import org.kryptonmc.api.entity.EquipmentSlot

object EquipmentSlots {

    private val BY_ARMOR_SLOT = mapOf(
        ArmorSlot.BOOTS to EquipmentSlot.FEET,
        ArmorSlot.LEGGINGS to EquipmentSlot.LEGS,
        ArmorSlot.CHESTPLATE to EquipmentSlot.CHEST,
        ArmorSlot.HELMET to EquipmentSlot.HEAD
    )
    private val BY_NAME = EquipmentSlot.values().associateBy { it.name.lowercase() }

    @JvmStatic
    fun fromArmorSlot(slot: ArmorSlot): EquipmentSlot = BY_ARMOR_SLOT.get(slot)!!

    @JvmStatic
    fun fromName(name: String): EquipmentSlot? = BY_NAME.get(name)

    @JvmStatic
    fun index(slot: EquipmentSlot): Int = when (slot.type) {
        EquipmentSlot.Type.HAND -> slot.ordinal
        EquipmentSlot.Type.ARMOR -> slot.ordinal - 2
    }
}
