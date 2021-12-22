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
package org.kryptonmc.krypton.world.block.entity

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.registry.Registries

private val BLOCK_ENTITY_TYPE_BY_BLOCK = Int2ObjectOpenHashMap<BlockEntityType>().apply {
    Registries.BLOCK_ENTITY_TYPE.forEach { entry ->
        entry.value.applicableBlocks.forEach {
            put(it.id, entry.value)
        }
    }
}

fun Block.blockEntityType(): BlockEntityType? = BLOCK_ENTITY_TYPE_BY_BLOCK[id]
