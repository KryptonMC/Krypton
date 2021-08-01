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
package org.kryptonmc.krypton.world.generation.flat

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType

data class FlatLayer(
    val block: Block,
    val height: Int
) {

    override fun toString() = "${if (height != 1) "$height*" else ""}${block.key}"

    companion object {

        val CODEC: Codec<FlatLayer> = RecordCodecBuilder.create {
            it.group(
                (Registries.BLOCK as KryptonRegistry<Block>).fieldOf("block").orElse(Blocks.AIR).forGetter(FlatLayer::block),
                Codec.intRange(0, KryptonDimensionType.Y_SIZE).fieldOf("height").forGetter(FlatLayer::height)
            ).apply(it, ::FlatLayer)
        }
    }
}
