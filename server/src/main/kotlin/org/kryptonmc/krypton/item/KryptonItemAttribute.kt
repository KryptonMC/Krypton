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
package org.kryptonmc.krypton.item

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.item.ItemAttribute
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.EquipmentSlots
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.nbt.CompoundTag

@JvmRecord
data class KryptonItemAttribute(
    override val type: AttributeType,
    override val slot: EquipmentSlot,
    override val modifier: AttributeModifier
) : ItemAttribute {

    object Factory : ItemAttribute.Factory {

        override fun of(type: AttributeType, slot: EquipmentSlot, modifier: AttributeModifier): ItemAttribute =
            KryptonItemAttribute(type, slot, modifier)
    }

    companion object {

        @JvmStatic
        fun from(data: CompoundTag): KryptonItemAttribute? {
            val type = Registries.ATTRIBUTE[Key.key(data.getString("AttributeName"))] ?: return null
            val slot = EquipmentSlots.fromName(data.getString("Slot")) ?: return null
            val modifier = KryptonAttributeModifier.from(data) ?: return null
            return KryptonItemAttribute(type, slot, modifier)
        }
    }
}
