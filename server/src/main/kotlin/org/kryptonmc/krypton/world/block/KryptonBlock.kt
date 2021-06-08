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
import org.kryptonmc.api.block.BoundingBox
import org.kryptonmc.api.inventory.item.Material
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.world.chunk.Chunk

data class KryptonBlock(
    override val type: Material,
    override val chunk: Chunk,
    override val location: Vector
) : Block {

    override val isEmpty = type == Material.AIR
    override val isLiquid = type == Material.WATER || type == Material.LAVA
    override val world = chunk.world
    override val boundingBox = BoundingBox.EMPTY
}
