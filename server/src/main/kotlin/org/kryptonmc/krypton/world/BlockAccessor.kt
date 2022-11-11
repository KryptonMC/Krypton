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

import org.kryptonmc.api.block.BlockContainer
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.fluid.FluidContainer
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids

interface BlockAccessor : BlockContainer, FluidContainer, HeightAccessor {

    val maximumLightLevel: Int
        get() = 15

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState

    override fun getFluid(position: Vec3i): KryptonFluidState = getFluid(position.x, position.y, position.z)

    override fun setBlock(x: Int, y: Int, z: Int, block: BlockState): Boolean = setBlock(x, y, z, block.downcast())

    override fun setBlock(position: Vec3i, block: BlockState): Boolean = setBlock(position, block.downcast())

    fun setBlock(x: Int, y: Int, z: Int, block: KryptonBlockState): Boolean

    fun setBlock(position: Vec3i, block: KryptonBlockState): Boolean = setBlock(position.x, position.y, position.z, block)

    object Empty : BlockAccessor {

        override val height: Int
            get() = 0
        override val minimumBuildHeight: Int
            get() = 0

        override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState = KryptonBlocks.AIR.defaultState

        override fun setBlock(x: Int, y: Int, z: Int, block: KryptonBlockState): Boolean = false

        override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState = KryptonFluids.EMPTY.defaultState
    }
}
