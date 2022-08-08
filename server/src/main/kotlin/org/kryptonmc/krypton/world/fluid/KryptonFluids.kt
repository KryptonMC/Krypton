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

import org.kryptonmc.api.registry.Registries

object KryptonFluids {

    @JvmField
    val EMPTY: KryptonFluid = register("empty", EmptyFluid())
    @JvmField
    val FLOWING_WATER: FlowingFluid = register("flowing_water", WaterFluid.Flowing())
    @JvmField
    val WATER: FlowingFluid = register("water", WaterFluid.Source())
    @JvmField
    val FLOWING_LAVA: FlowingFluid = register("flowing_lava", LavaFluid.Flowing())
    @JvmField
    val LAVA: FlowingFluid = register("lava", LavaFluid.Source())

    init {
        Registries.FLUID.values.forEach { fluid ->
            (fluid as KryptonFluid).stateDefinition.states.forEach { KryptonFluid.STATES.add(it) }
        }
    }

    @JvmStatic
    private fun <T : KryptonFluid> register(key: String, fluid: T): T = Registries.FLUID.register(key, fluid)
}
