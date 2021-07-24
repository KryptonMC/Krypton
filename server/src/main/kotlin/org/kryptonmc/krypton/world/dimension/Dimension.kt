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
import org.kryptonmc.krypton.util.nonNullSupplier
import org.kryptonmc.krypton.world.generation.Generator
import java.util.function.Supplier

data class Dimension(
    val typeSupplier: Supplier<DimensionType>,
    val generator: Generator
) {

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
    }
}
