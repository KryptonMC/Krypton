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

import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.Vec3dImpl
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter

class EmptyFluid : KryptonFluid() {

    override val bucket: ItemType
        get() = ItemTypes.AIR.get()
    override val explosionResistance: Double
        get() = 0.0
    override val isEmpty: Boolean
        get() = true

    override fun getFlow(world: BlockGetter, pos: BlockPos, state: KryptonFluidState): Vec3d = Vec3dImpl.ZERO

    override fun getHeight(state: KryptonFluidState, world: BlockGetter, pos: BlockPos): Float = 0F

    override fun getOwnHeight(state: KryptonFluidState): Float = 0F

    override fun asBlock(state: KryptonFluidState): KryptonBlockState = KryptonBlocks.AIR.defaultState

    override fun isSource(state: KryptonFluidState): Boolean = false

    override fun level(state: KryptonFluidState): Int = 0

    override fun getShape(state: KryptonFluidState, world: BlockGetter, pos: BlockPos): VoxelShape = Shapes.empty()
}
