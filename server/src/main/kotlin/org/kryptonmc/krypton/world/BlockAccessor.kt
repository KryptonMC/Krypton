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
package org.kryptonmc.krypton.world

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntity
import org.kryptonmc.api.fluid.Fluid
import org.spongepowered.math.vector.Vector3i

interface BlockAccessor : HeightAccessor {

    val maximumLightLevel: Int
        get() = 15

    fun getBlock(x: Int, y: Int, z: Int): Block

    fun getBlock(position: Vector3i): Block = getBlock(position.x(), position.y(), position.z())

    fun <T : BlockEntity> getBlockEntity(x: Int, y: Int, z: Int): T?

    fun <T : BlockEntity> getBlockEntity(position: Vector3i): T? = getBlockEntity(position.x(), position.y(), position.z())

    fun getFluid(x: Int, y: Int, z: Int): Fluid

    fun getFluid(position: Vector3i): Fluid = getFluid(position.x(), position.y(), position.z())

    fun setBlock(x: Int, y: Int, z: Int, block: Block): Boolean

    fun setBlock(position: Vector3i, block: Block): Boolean = setBlock(position.x(), position.y(), position.z(), block)
}
