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

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.StateDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.BlockPos
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

    override val isSource: Boolean
        get() = fluid.isSource(this)
    override val level: Int
        get() = fluid.level(this)
    val ownHeight: Float
        get() = fluid.getOwnHeight(this)

    fun getHeight(world: BlockGetter, pos: BlockPos): Float = fluid.getHeight(this, world, pos)

    fun getFlow(world: BlockGetter, pos: BlockPos): Vec3d = fluid.getFlow(world, pos, this)

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
