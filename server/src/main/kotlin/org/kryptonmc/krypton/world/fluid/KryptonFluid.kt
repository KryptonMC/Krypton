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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.StateHolderDelegate
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter

@Suppress("LeakingThis")
abstract class KryptonFluid : Fluid, StateHolderDelegate<FluidState, KryptonFluidState> {

    final override val stateDefinition: StateDefinition<KryptonFluid, KryptonFluidState>
    private var defaultFluidState: KryptonFluidState
    val builtInRegistryHolder: Holder.Reference<KryptonFluid> = KryptonRegistries.FLUID.createIntrusiveHolder(this)
    final override val defaultState: KryptonFluidState
        get() = defaultFluidState

    override val isEmpty: Boolean
        get() = false

    init {
        val builder = StateDefinition.Builder<KryptonFluid, KryptonFluidState>(this)
        createStateDefinition(builder)
        stateDefinition = builder.build({ it.defaultFluidState }, ::KryptonFluidState)
        defaultFluidState = stateDefinition.any()
    }

    open fun pickupSound(): SoundEvent? = null

    abstract fun getFlow(world: BlockGetter, pos: BlockPos, state: KryptonFluidState): Vec3d

    abstract fun getHeight(state: KryptonFluidState, world: BlockGetter, pos: BlockPos): Float

    abstract fun getOwnHeight(state: KryptonFluidState): Float

    abstract fun asBlock(state: KryptonFluidState): KryptonBlockState

    abstract fun isSource(state: KryptonFluidState): Boolean

    abstract fun level(state: KryptonFluidState): Int

    open fun isSame(fluid: Fluid): Boolean = fluid === this

    abstract fun getShape(state: KryptonFluidState, world: BlockGetter, pos: BlockPos): VoxelShape

    override fun key(): Key = KryptonRegistries.FLUID.getKey(this)

    @Suppress("UNCHECKED_CAST")
    fun eq(tag: TagKey<Fluid>): Boolean = builtInRegistryHolder.eq(tag as TagKey<KryptonFluid>)

    protected fun registerDefaultState(state: KryptonFluidState) {
        defaultFluidState = state
    }

    protected open fun createStateDefinition(builder: StateDefinition.Builder<KryptonFluid, KryptonFluidState>) {
        // Do nothing by default
    }

    companion object {

        @JvmField
        val STATES: IntHashBiMap<KryptonFluidState> = IntHashBiMap()
    }
}
