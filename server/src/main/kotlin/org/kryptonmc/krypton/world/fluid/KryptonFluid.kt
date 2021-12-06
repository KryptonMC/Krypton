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

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidHandler
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.world.block.property.KryptonPropertyHolder
import org.kryptonmc.krypton.world.fluid.handler.EmptyFluidHandler

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

    override val handler: FluidHandler
        get() = KryptonFluidManager.handler(this) ?: EmptyFluidHandler

    override fun key(): Key = key

    override fun copy(key: String, value: String): Fluid {
        val newProperties = properties + (key to value)
        return requireNotNull(FluidLoader.properties(key().asString(), newProperties)) { "Invalid property $key:$value for block ${key()}" }
    }

    override fun copy(newValues: Map<String, String>): Fluid {
        val newProperties = properties + newValues
        return requireNotNull(FluidLoader.properties(key().asString(), newProperties)) { "Invalid properties $newValues for block ${key()}!" }
    }

    override fun asBlock() = Registries.BLOCK[blockKey] ?: Blocks.AIR

    override fun toBuilder() = Builder(this)

    class Builder(
        private val key: Key,
        private var id: Int,
        private var stateId: Int
    ) : KryptonPropertyHolder.Builder<Fluid.Builder, Fluid>(), Fluid.Builder {

        private var bucket = ItemTypes.AIR
        private var empty = false
        private var resistance = 0.0
        private var source = false
        private var height = 0F
        private var level = 0
        private var block: Key? = null

        constructor(fluid: Fluid) : this(fluid.key(), fluid.id, fluid.stateId) {
            bucket = fluid.bucket
            empty = fluid.isEmpty
            resistance = fluid.explosionResistance
            source = fluid.isSource
            height = fluid.height
            level = fluid.level
            block = fluid.asBlock().key()
        }

        override fun id(id: Int): Fluid.Builder = apply { this.id = id }

        override fun stateId(id: Int): Fluid.Builder = apply { stateId = id }

        override fun bucket(type: ItemType): Fluid.Builder = apply { bucket = type }

        override fun empty(value: Boolean): Fluid.Builder = apply { empty = value }

        override fun resistance(resistance: Double): Fluid.Builder = apply { this.resistance = resistance }

        override fun source(value: Boolean): Fluid.Builder = apply { source = value }

        override fun height(height: Float): Fluid.Builder = apply { this.height = height }

        override fun level(level: Int): Fluid.Builder = apply { this.level = level }

        override fun block(key: Key): Fluid.Builder = apply { block = key }

        override fun build() = KryptonFluid(
            key,
            id,
            stateId,
            bucket,
            empty,
            resistance,
            source,
            height,
            level,
            block ?: AIR_KEY,
            ImmutableSet.copyOf(availableProperties),
            ImmutableMap.copyOf(properties)
        )
    }

    object Factory : Fluid.Factory {

        override fun builder(key: Key, id: Int, stateId: Int) = Builder(key, id, stateId)

        override fun fromId(id: Int): Fluid? = Registries.FLUID[id]

        override fun fromStateId(id: Int): Fluid? = FluidLoader.STATES[id]
    }

    companion object {

        private val AIR_KEY = Key.key("air")
    }
}
