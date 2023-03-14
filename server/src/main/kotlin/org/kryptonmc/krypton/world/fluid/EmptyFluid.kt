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
package org.kryptonmc.krypton.world.fluid

import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter

class EmptyFluid : KryptonFluid() {

    override val bucket: ItemType
        get() = ItemTypes.AIR.get()
    override val explosionResistance: Double
        get() = 0.0

    override fun isEmpty(): Boolean = true

    override fun getFlow(world: BlockGetter, pos: Vec3i, state: KryptonFluidState): Vec3d = Vec3d.ZERO

    override fun getHeight(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): Float = 0F

    override fun getOwnHeight(state: KryptonFluidState): Float = 0F

    override fun asBlock(state: KryptonFluidState): KryptonBlockState = KryptonBlocks.AIR.defaultState

    override fun isSource(state: KryptonFluidState): Boolean = false

    override fun level(state: KryptonFluidState): Int = 0

    override fun getShape(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): VoxelShape = Shapes.empty()
}
