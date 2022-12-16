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

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

abstract class LavaFluid : FlowingFluid() {

    override val flowing: KryptonFluid
        get() = KryptonFluids.FLOWING_LAVA
    override val source: KryptonFluid
        get() = KryptonFluids.LAVA
    override val bucket: ItemType
        get() = ItemTypes.LAVA_BUCKET
    override val explosionResistance: Double
        get() = 100.0

    override fun isSame(fluid: Fluid): Boolean = fluid === KryptonFluids.LAVA || fluid === KryptonFluids.FLOWING_LAVA

    override fun asBlock(state: KryptonFluidState): KryptonBlockState = KryptonBlocks.LAVA.defaultState.setProperty(LEVEL, calculateBlockLevel(state))

    class Flowing : LavaFluid() {

        override fun createStateDefinition(builder: StateDefinition.Builder<KryptonFluid, KryptonFluidState>) {
            super.createStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun level(state: KryptonFluidState): Int = state.requireProperty(LEVEL)

        override fun isSource(state: KryptonFluidState): Boolean = false
    }

    class Source : LavaFluid() {

        override fun level(state: KryptonFluidState): Int = MAX_LEVEL

        override fun isSource(state: KryptonFluidState): Boolean = false
    }
}
