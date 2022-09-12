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
package org.kryptonmc.krypton.world

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.krypton.util.BitStorage
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.SimpleBitStorage
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import java.util.EnumSet

class Heightmap(private val chunk: ChunkAccessor, val type: Type) {

    val data: BitStorage = SimpleBitStorage((chunk.height + 1).ceillog2(), 256)

    fun update(x: Int, y: Int, z: Int, block: KryptonBlock): Boolean {
        val firstAvailable = firstAvailable(x, z)
        if (y <= firstAvailable - 2) return false

        if (type.isOpaque(block)) {
            if (y >= firstAvailable) {
                set(x, z, y + 1)
                return true
            }
        } else if (firstAvailable - 1 == y) {
            for (i in y - 1 downTo chunk.minimumBuildHeight) {
                if (type.isOpaque(chunk.getBlock(x, i, z))) {
                    set(x, z, i + 1)
                    return true
                }
            }
            set(x, z, chunk.minimumBuildHeight)
            return true
        }
        return false
    }

    fun setData(chunk: ChunkAccessor, type: Type, rawData: LongArray) {
        val current = data.data
        if (current.size == rawData.size) {
            System.arraycopy(rawData, 0, current, 0, rawData.size)
        } else {
            LOGGER.warn("Ignoring heightmap data for chunk ${chunk.position} as the size is not what was expected. " +
                    "Expected ${current.size}, got ${rawData.size}.")
            prime(chunk, setOf(type))
        }
    }

    fun firstAvailable(x: Int, z: Int): Int = firstAvailable(indexOf(x, z))

    private fun set(x: Int, z: Int, y: Int) {
        data.set(indexOf(x, z), y - chunk.minimumBuildHeight)
    }

    private fun indexOf(x: Int, z: Int): Int = x + z * 16

    private fun firstAvailable(index: Int): Int = data.get(index) + chunk.minimumBuildHeight

    enum class Type(val isOpaque: (KryptonBlock) -> Boolean, val sendToClient: Boolean = false) {

        WORLD_SURFACE(NOT_AIR, true),
        WORLD_SURFACE_WG(NOT_AIR),
        OCEAN_FLOOR(BLOCKS_MOTION),
        OCEAN_FLOOR_WG(BLOCKS_MOTION),
        MOTION_BLOCKING({ it.blocksMotion || it.isLiquid }, true),
        MOTION_BLOCKING_NO_LEAVES({ (it.blocksMotion || it.isLiquid) && !IS_LEAVES(it) });

        companion object {

            @JvmField
            val POST_FEATURES: Set<Type> = EnumSet.of(WORLD_SURFACE, OCEAN_FLOOR, MOTION_BLOCKING, MOTION_BLOCKING_NO_LEAVES)
        }
    }

    companion object {

        private val NOT_AIR: (KryptonBlock) -> Boolean = { !it.isAir }
        private val BLOCKS_MOTION: (KryptonBlock) -> Boolean = { it.blocksMotion }
        private val IS_LEAVES: (KryptonBlock) -> Boolean = {
            it.id == Blocks.OAK_LEAVES.downcast().id ||
                    it.id == Blocks.SPRUCE_LEAVES.downcast().id ||
                    it.id == Blocks.BIRCH_LEAVES.downcast().id ||
                    it.id == Blocks.JUNGLE_LEAVES.downcast().id ||
                    it.id == Blocks.ACACIA_LEAVES.downcast().id ||
                    it.id == Blocks.DARK_OAK_LEAVES.downcast().id ||
                    it.id == Blocks.AZALEA_LEAVES.downcast().id ||
                    it.id == Blocks.FLOWERING_AZALEA_LEAVES.downcast().id
        }
        private val LOGGER = logger<Heightmap>()

        @JvmStatic
        fun prime(chunk: ChunkAccessor, toPrime: Set<Type>) {
            val size = toPrime.size
            val heightmaps = ObjectArrayList<Heightmap>(size)
            val iterator = heightmaps.iterator()
            val highest = chunk.highestSectionY + 16
            for (x in 0 until 16) {
                for (z in 0 until 16) {
                    toPrime.forEach { heightmaps.add(chunk.getOrCreateHeightmap(it)) }
                    y@ for (y in highest - 1 downTo chunk.minimumBuildHeight) {
                        val block = chunk.getBlock(x, y, z)
                        if (block === Blocks.AIR) continue@y
                        iterator@ while (iterator.hasNext()) {
                            val heightmap = iterator.next()
                            if (!heightmap.type.isOpaque(block)) continue@iterator
                            heightmap.set(x, z, y + 1)
                            iterator.remove()
                        }
                        if (heightmaps.isEmpty) break@y
                        iterator.back(size)
                    }
                }
            }
        }
    }
}
