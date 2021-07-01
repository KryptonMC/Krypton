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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key

data class BlockData(
    val name: String,
    val defaultState: Short,
    val hardness: Double,
    val resistance: Double,
    val isAir: Boolean,
    val isSolid: Boolean,
    val isBlockEntity: Boolean,
    private val hasSingleState: Boolean
) {

    val alternatives = mutableListOf<BlockAlternative>()
    val key = Key.key(name)
    val isLiquid = "water" in name.lowercase() || "lava" in name.lowercase()
    val instantlyBreaks = hardness == 0.0

    init {
        if (hasSingleState) addAlternative(BlockAlternative(defaultState))
    }

    fun addAlternative(alternative: BlockAlternative) {
        alternatives += alternative
        BlockArray.BLOCKS[alternative.id.toInt()] = this
    }
}

private object BlockArray {

    val BLOCKS = arrayOfNulls<BlockData>(Short.MAX_VALUE.toInt())
}
