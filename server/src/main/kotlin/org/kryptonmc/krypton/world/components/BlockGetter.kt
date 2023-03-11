/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
