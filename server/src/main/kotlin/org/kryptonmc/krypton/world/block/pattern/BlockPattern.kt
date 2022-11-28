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
package org.kryptonmc.krypton.world.block.pattern

import com.github.benmanes.caffeine.cache.CacheLoader
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.world.components.WorldAccessor
import java.util.function.Predicate
import kotlin.math.max

class BlockPattern(private val pattern: Array<Array<Array<Predicate<BlockInWorld?>>>>) {

    val depth: Int = pattern.size
    val height: Int = if (depth > 0) pattern[0].size else 0
    val width: Int = if (depth > 0 && height > 0) pattern[0][0].size else 0

    fun find(world: WorldAccessor, position: BlockPos): BlockPatternMatch? {
        val cache = createCache(world, false)
        val max = max(max(width, height), depth)
        BlockPos.betweenClosed(position, position.add(max - 1, max - 1, max - 1)).forEach { pos ->
            Direction.values().forEach { forwards ->
                Direction.values().forEach upper@{ up ->
                    if (forwards == up || up == forwards.opposite) return@upper
                    val match = matches(pos, forwards, up, cache)
                    if (match != null) return match
                }
            }
        }
        return null
    }

    private fun matches(position: BlockPos, forwards: Direction, up: Direction, cache: LoadingCache<BlockPos, BlockInWorld>): BlockPatternMatch? {
        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until depth) {
                    if (!pattern[x][y][z].test(cache.get(translateAndRotate(position, forwards, up, x, y, z)))) return null
                }
            }
        }
        return BlockPatternMatch(position, forwards, up, cache, width, height, depth)
    }

    private class BlockCacheLoader(private val world: WorldAccessor, private val loadChunks: Boolean) : CacheLoader<BlockPos, BlockInWorld> {

        override fun load(key: BlockPos): BlockInWorld = BlockInWorld(world, key, loadChunks)
    }

    class BlockPatternMatch(
        val frontTopLeft: BlockPos,
        val forwards: Direction,
        val up: Direction,
        private val cache: LoadingCache<BlockPos, BlockInWorld>,
        val width: Int,
        val height: Int,
        val depth: Int
    ) {

        fun getBlock(x: Int, y: Int, z: Int): BlockInWorld = cache.get(translateAndRotate(frontTopLeft, forwards, up, x, y, z))

        override fun toString(): String = "BlockPatternMatch(up=$up, forwards=$forwards, frontTopLeft=$frontTopLeft)"
    }

    companion object {

        @JvmStatic
        private fun translateAndRotate(position: BlockPos, forwards: Direction, up: Direction, x: Int, y: Int, z: Int): BlockPos {
            require(forwards != up && forwards != up.opposite) { "Invalid forwards and up combination!" }
            val crossed = forwards.normal.cross(up.normal)
            return position.add(
                up.normalX * -y + crossed.x * x + forwards.normalX * z,
                up.normalY * -y + crossed.y * x + forwards.normalY * z,
                up.normalZ * -y + crossed.z * x + forwards.normalZ * z
            )
        }

        @JvmStatic
        private fun createCache(world: WorldAccessor, loadChunks: Boolean): LoadingCache<BlockPos, BlockInWorld> =
            Caffeine.newBuilder().build(BlockCacheLoader(world, loadChunks))
    }
}
