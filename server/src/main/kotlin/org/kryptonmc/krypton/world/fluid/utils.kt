/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidHandler
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.world.fluid.handler.EmptyFluidHandler

val Fluid.handler: FluidHandler
    get() = KryptonFluidManager.handler(this) ?: EmptyFluidHandler

private val DIRECT_FLUID_CODEC: Codec<Fluid> = RecordCodecBuilder.create {
    it.group(
        KEY_CODEC.fieldOf("Name").forGetter(Fluid::key),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("Properties").forGetter(Fluid::properties)
    ).apply(it) { key, properties -> FluidLoader.fromKey(key)!!.copy(properties) }
}

val BLOCK_CODEC: Codec<Fluid> = InternalRegistries.FLUID.dispatch("Name", { it }) { DIRECT_FLUID_CODEC }.stable()
