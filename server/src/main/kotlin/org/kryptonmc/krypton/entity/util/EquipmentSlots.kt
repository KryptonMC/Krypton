/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.util

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
