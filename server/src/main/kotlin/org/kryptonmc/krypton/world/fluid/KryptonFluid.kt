/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.fluid

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.block.property.KryptonPropertyHolder

@JvmRecord
data class KryptonFluid(
    private val key: Key,
    override val id: Int,
    override val stateId: Int,
    override val bucket: ItemType,
    override val isEmpty: Boolean,
    override val explosionResistance: Double,
    override val isSource: Boolean,
    override val height: Float,
    override val level: Int,
    val blockKey: Key,
    override val availableProperties: Set<Property<*>>,
    override val properties: Map<String, String>
) : KryptonPropertyHolder<Fluid>, Fluid {

    override fun copy(key: String, value: String): Fluid {
        val newProperties = properties + (key to value)
        return requireNotNull(FluidLoader.properties(this.key().asString(), newProperties)) {
            "Invalid property $key:$value for block ${this.key()}"
        }
    }

    override fun copy(newValues: Map<String, String>): Fluid {
        val newProperties = properties + newValues
        return requireNotNull(FluidLoader.properties(key().asString(), newProperties)) {
            "Invalid properties $newValues for block ${key()}!"
        }
    }

    override fun asBlock() = InternalRegistries.BLOCK[blockKey] ?: Blocks.AIR

    override fun key(): Key = key

    override fun compareTo(other: Fluid) = id.compareTo(other.id)
}
