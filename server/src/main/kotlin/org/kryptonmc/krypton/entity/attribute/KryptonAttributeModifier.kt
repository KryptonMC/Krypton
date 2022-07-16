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
package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID

@JvmRecord
data class KryptonAttributeModifier(
    override val name: String,
    override val uuid: UUID,
    override val amount: Double,
    override val operation: ModifierOperation
) : AttributeModifier {

    object Factory : AttributeModifier.Factory {

        override fun of(name: String, uuid: UUID, amount: Double, operation: ModifierOperation): AttributeModifier =
            KryptonAttributeModifier(name, uuid, amount, operation)
    }

    companion object {

        @JvmStatic
        fun from(data: CompoundTag): KryptonAttributeModifier? {
            val operation = KryptonRegistries.MODIFIER_OPERATIONS.get(data.getInt("Operation")) ?: return null
            val uuid = data.getUUID("UUID") ?: return null
            return KryptonAttributeModifier(data.getString("Name"), uuid, data.getDouble("Amount"), operation)
        }
    }
}
