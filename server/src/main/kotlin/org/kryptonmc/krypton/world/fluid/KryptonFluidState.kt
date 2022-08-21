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

import kotlinx.collections.immutable.ImmutableMap
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.StateDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import org.spongepowered.math.vector.Vector3d

class KryptonFluidState(
    override val fluid: KryptonFluid,
    values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    propertiesCodec: MapCodec<KryptonFluidState>
) : KryptonState<KryptonFluid, KryptonFluidState>(fluid, values, propertiesCodec), FluidState,
    StateDelegate<FluidState, KryptonFluidState> {

    override val state: KryptonFluidState
        get() = this
    override val isSource: Boolean
        get() = fluid.isSource(this)
    override val level: Int
        get() = fluid.level(this)
    val ownHeight: Float
        get() = fluid.getOwnHeight(this)

    fun getHeight(world: BlockAccessor, x: Int, y: Int, z: Int): Float = fluid.getHeight(this, world, x, y, z)

    fun getFlow(world: BlockAccessor, x: Int, y: Int, z: Int): Vector3d = fluid.getFlow(world, x, y, z, this)

    fun eq(tag: Tag<Fluid>): Boolean = tag.contains(fluid)

    fun eq(block: Fluid): Boolean = this.fluid === block

    override fun asBlock(): BlockState = fluid.asBlock(this)

    companion object {

        // FIXME: Don't cast this when we update the way we do registries.
        @JvmField
        @Suppress("UNCHECKED_CAST")
        val CODEC: Codec<KryptonFluidState> = codec(KryptonRegistries.FLUID.byNameCodec() as Codec<KryptonFluid>, KryptonFluid::defaultState).stable()
    }
}
