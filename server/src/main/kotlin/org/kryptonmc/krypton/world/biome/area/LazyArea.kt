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
package org.kryptonmc.krypton.world.biome.area

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap
import org.kryptonmc.krypton.world.biome.layer.traits.PixelTransformer
import org.kryptonmc.krypton.world.chunk.ChunkPosition

class LazyArea(
    private val cache: Long2IntLinkedOpenHashMap,
    val maxCache: Int,
    private val transformer: PixelTransformer
) : Area {

    override fun get(x: Int, z: Int): Int {
        val encoded = ChunkPosition.toLong(x, z)
        synchronized(cache) {
            val cached = cache[encoded]
            if (cached != Int.MAX_VALUE) return cached
            val transformed = transformer(x, z)
            cache[encoded] = transformed
            if (cache.size > maxCache) for (i in 0 until maxCache / 16) cache.removeFirstInt()
            return transformed
        }
    }
}
