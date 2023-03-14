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

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

abstract class WaterFluid : FlowingFluid() {

    override val bucket: ItemType
        get() = ItemTypes.WATER_BUCKET.get()
    override val explosionResistance: Double
        get() = 100.0

    override fun flowing(): KryptonFluid = KryptonFluids.FLOWING_WATER

    override fun source(): KryptonFluid = KryptonFluids.WATER

    override fun isSame(fluid: Fluid): Boolean = fluid === source() || fluid === flowing()

    override fun asBlock(state: KryptonFluidState): KryptonBlockState =
        KryptonBlocks.WATER.defaultState.setProperty(LEVEL, calculateBlockLevel(state))

    class Flowing : WaterFluid() {

        override fun createStateDefinition(builder: StateDefinition.Builder<KryptonFluid, KryptonFluidState>) {
            super.createStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun level(state: KryptonFluidState): Int = state.requireProperty(LEVEL)

        override fun isSource(state: KryptonFluidState): Boolean = false
    }

    class Source : WaterFluid() {

        override fun level(state: KryptonFluidState): Int = MAX_LEVEL

        override fun isSource(state: KryptonFluidState): Boolean = true
    }
}
