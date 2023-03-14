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

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.StateDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import java.util.stream.Stream

class KryptonFluidState(
    override val fluid: KryptonFluid,
    values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    propertiesCodec: MapCodec<KryptonFluidState>
) : KryptonState<KryptonFluid, KryptonFluidState>(fluid, values, propertiesCodec), FluidState, StateDelegate<FluidState, KryptonFluidState> {

    override val level: Int
        get() = fluid.level(this)

    override fun isSource(): Boolean = fluid.isSource(this)

    fun ownHeight(): Float = fluid.getOwnHeight(this)

    fun getHeight(world: BlockGetter, pos: Vec3i): Float = fluid.getHeight(this, world, pos)

    fun getFlow(world: BlockGetter, pos: Vec3i): Vec3d = fluid.getFlow(world, pos, this)

    @Suppress("UNCHECKED_CAST")
    fun eq(tag: TagKey<Fluid>): Boolean = fluid.builtInRegistryHolder.eq(tag as TagKey<KryptonFluid>)

    fun eq(block: Fluid): Boolean = this.fluid === block

    fun tags(): Stream<TagKey<KryptonFluid>> = owner.builtInRegistryHolder.tags()

    override fun asBlock(): KryptonBlockState = fluid.asBlock(this)

    override fun asState(): KryptonFluidState = this

    companion object {

        @JvmField
        val CODEC: Codec<KryptonFluidState> = codec(KryptonRegistries.FLUID.byNameCodec(), KryptonFluid::defaultState).stable()
    }
}
