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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.api.block.BlockContainer
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids

interface BlockGetter : HeightAccessor, BlockContainer, FluidGetter, BlockEntityGetter {

    fun maximumLightLevel(): Int = 15

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    object Empty : BlockGetter {

        override fun height(): Int = 0

        override fun minimumBuildHeight(): Int = 0

        override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState = KryptonBlocks.AIR.defaultState

        override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState = KryptonFluids.EMPTY.defaultState

        override fun getBlockEntity(x: Int, y: Int, z: Int): KryptonBlockEntity? = null
    }
}
