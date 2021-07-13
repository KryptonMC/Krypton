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

import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.registry.tags.TagManager
import org.kryptonmc.krypton.registry.tags.TagType

private val BLOCK_TAGS = TagManager.tags[TagType.BLOCKS.identifier]!!

private val LEAVES_TAGS = BLOCK_TAGS.first { it.name.value() == "leaves" }.values

val Block.isLeaves: Boolean
    get() = key in LEAVES_TAGS

private val NEEDS_STONE_TAGS = BLOCK_TAGS.first { it.name.value() == "needs_stone_tool" }.values
private val NEEDS_IRON_TAGS = BLOCK_TAGS.first { it.name.value() == "needs_iron_tool" }.values
private val NEEDS_DIAMOND_TAGS = BLOCK_TAGS.first { it.name.value() == "needs_diamond_tool" }.values

val Block.needsStone: Boolean
    get() = key in NEEDS_STONE_TAGS

val Block.needsIron: Boolean
    get() = key in NEEDS_IRON_TAGS

val Block.needsDiamond: Boolean
    get() = key in NEEDS_DIAMOND_TAGS
