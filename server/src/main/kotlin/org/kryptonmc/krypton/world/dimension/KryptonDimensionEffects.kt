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
package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.dimension.DimensionEffect

object KryptonDimensionEffects {

    @JvmField val OVERWORLD: DimensionEffect = register("overworld", true, true, false, false)
    @JvmField val THE_NETHER: DimensionEffect = register("the_nether", false, false, true, false)
    @JvmField val THE_END: DimensionEffect = register("the_end", false, false, false, true)

    @JvmStatic
    private fun register(name: String, clouds: Boolean, celestialBodies: Boolean, fog: Boolean, endSky: Boolean): KryptonDimensionEffect {
        val key = Key.key(name)
        return Registries.DIMENSION_EFFECTS.register(key, KryptonDimensionEffect(key, clouds, celestialBodies, fog, endSky))
    }
}
