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
package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.nbt.ListTag

sealed class GlobalPalette<T>(override val size: Int, private val registry: IntBiMap<T>) : Palette<T> {

    override val serializedSize = 0

    override fun get(id: Int): T = registry[id] ?: throw MissingPaletteEntryException(id)

    override fun get(value: T): Int = registry.idOf(value)

    override fun load(data: ListTag) = Unit

    override fun write(buf: ByteBuf) = Unit

    object Blocks : GlobalPalette<Block>(BlockLoader.STATES.size, BlockLoader.STATES)

    object Biomes : GlobalPalette<Biome>(Registries.BIOME.size, InternalRegistries.BIOME)
}
