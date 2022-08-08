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
package org.kryptonmc.krypton.world.fluid

import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.downcast
import org.spongepowered.math.vector.Vector3d

class EmptyFluid : KryptonFluid() {

    override val bucket: ItemType
        get() = ItemTypes.AIR
    override val explosionResistance: Double
        get() = 0.0
    override val isEmpty: Boolean
        get() = true

    override fun getFlow(world: BlockAccessor, x: Int, y: Int, z: Int, state: KryptonFluidState): Vector3d = Vector3d.ZERO

    override fun getHeight(state: KryptonFluidState, world: BlockAccessor, x: Int, y: Int, z: Int): Float = 0F

    override fun getOwnHeight(state: KryptonFluidState): Float = 0F

    override fun asBlock(state: KryptonFluidState): KryptonBlockState = Blocks.AIR.defaultState.downcast()

    override fun isSource(state: KryptonFluidState): Boolean = false

    override fun level(state: KryptonFluidState): Int = 0

    override fun getShape(state: KryptonFluidState, world: BlockAccessor, x: Int, y: Int, z: Int): VoxelShape = Shapes.empty()
}
