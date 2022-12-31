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

import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.world.components.WorldAccessor
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import java.util.function.Predicate

class BlockInWorld(val world: WorldAccessor, val position: BlockPos, private val loadChunks: Boolean) {

    private var blockState: KryptonBlockState? = null
    private var blockEntity: KryptonBlockEntity? = null
    private var cachedEntity = false

    fun state(): KryptonBlockState {
        if (blockState == null && (loadChunks || world.hasChunkAt(position))) blockState = world.getBlock(position)
        return blockState!!
    }

    fun entity(): KryptonBlockEntity? {
        if (blockEntity == null && !cachedEntity) {
            blockEntity = world.getBlockEntity(position) as? KryptonBlockEntity
            cachedEntity = true
        }
        return blockEntity
    }

    companion object {

        @JvmStatic
        fun hasState(predicate: Predicate<KryptonBlockState?>): Predicate<BlockInWorld?> = Predicate { it != null && predicate.test(it.state()) }
    }
}
