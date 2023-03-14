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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.StateHolderDelegate
import org.kryptonmc.krypton.util.map.IntHashBiMap
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter

@Suppress("LeakingThis")
abstract class KryptonFluid : Fluid, StateHolderDelegate<FluidState, KryptonFluidState> {

    final override val stateDefinition: StateDefinition<KryptonFluid, KryptonFluidState>
    private var defaultFluidState: KryptonFluidState
    val builtInRegistryHolder: Holder.Reference<KryptonFluid> = KryptonRegistries.FLUID.createIntrusiveHolder(this)
    final override val defaultState: KryptonFluidState
        get() = defaultFluidState

    init {
        val builder = StateDefinition.Builder<KryptonFluid, KryptonFluidState>(this)
        createStateDefinition(builder)
        stateDefinition = builder.build({ it.defaultFluidState }, ::KryptonFluidState)
        defaultFluidState = stateDefinition.any()
    }

    override fun isEmpty(): Boolean = false

    open fun pickupSound(): SoundEvent? = null

    abstract fun getFlow(world: BlockGetter, pos: Vec3i, state: KryptonFluidState): Vec3d

    abstract fun getHeight(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): Float

    abstract fun getOwnHeight(state: KryptonFluidState): Float

    abstract fun asBlock(state: KryptonFluidState): KryptonBlockState

    abstract fun isSource(state: KryptonFluidState): Boolean

    abstract fun level(state: KryptonFluidState): Int

    open fun isSame(fluid: Fluid): Boolean = fluid === this

    abstract fun getShape(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): VoxelShape

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
