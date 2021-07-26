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
package org.kryptonmc.krypton.world.dimension

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.nonNullSupplier
import org.kryptonmc.krypton.world.generation.Generator

data class Dimension(
    val typeSupplier: () -> DimensionType,
    val generator: Generator
) {

    val type: DimensionType
        get() = typeSupplier()

    constructor(generator: Generator, typeSupplier: () -> DimensionType) : this(typeSupplier, generator)

    companion object {

        val OVERWORLD = ResourceKey.of(InternalResourceKeys.DIMENSION, key("overworld"))
        val NETHER = ResourceKey.of(InternalResourceKeys.DIMENSION, key("the_nether"))
        val END = ResourceKey.of(InternalResourceKeys.DIMENSION, key("the_end"))
        val CODEC: Codec<Dimension> = RecordCodecBuilder.create {
            it.group(
                DimensionTypes.CODEC.fieldOf("type").flatXmap(nonNullSupplier(), nonNullSupplier()).forGetter(Dimension::typeSupplier),
                Generator.CODEC.fieldOf("generator").forGetter(Dimension::generator)
            ).apply(it, ::Dimension)
        }
        private val BUILT_IN_ORDER = setOf(OVERWORLD, NETHER, END)

        fun KryptonRegistry<Dimension>.sort(): KryptonRegistry<Dimension> {
            val registry = KryptonRegistry(InternalResourceKeys.DIMENSION)
            BUILT_IN_ORDER.forEach { key -> get(key)?.let { registry.register(key, it) } }
            entries.forEach { (key, value) -> if (key !in BUILT_IN_ORDER) registry.register(key, value) }
            return registry
        }

        fun KryptonRegistry<Dimension>.stable(seed: Long): Boolean {
            val entries = entries.toList()
            if (entries.size != BUILT_IN_ORDER.size) return false
            val overworld = entries[0]
            val nether = entries[1]
            val end = entries[2]
            if (overworld.key != OVERWORLD || nether.key != NETHER || end.key != END) return false
            if (!overworld.value.type.equalTo(DimensionTypes.OVERWORLD) && !overworld.value.type.equalTo(DimensionTypes.OVERWORLD_CAVES)) return false
            if (!nether.value.type.equalTo(DimensionTypes.THE_NETHER)) return false
            if (!end.value.type.equalTo(DimensionTypes.THE_END)) return false
            // TODO: Generator checking
            return true
        }
    }
}
