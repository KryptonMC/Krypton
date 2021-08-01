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
package org.kryptonmc.krypton.world.biome.layer

import org.kryptonmc.krypton.world.biome.BiomeConstants
import org.kryptonmc.krypton.world.biome.area.Area
import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.AreaTransformer2
import org.kryptonmc.krypton.world.biome.layer.traits.DimensionOffset0Transformer

object OceanMixerLayer : AreaTransformer2, DimensionOffset0Transformer {

    override fun invoke(context: Context, firstParent: Area, secondParent: Area, x: Int, z: Int): Int {
        val firstValue = firstParent[getParentX(x), getParentZ(z)]
        val secondValue = secondParent[getParentX(x), getParentZ(z)]
        if (!firstValue.isOcean()) return firstValue
        for (i in -8..8 step 4) {
            for (j in -8..8 step 4) {
                val value = firstParent[getParentX(x + i), getParentZ(z + j)]
                if (!value.isOcean()) {
                    if (secondValue == BiomeConstants.WARM_OCEAN) return BiomeConstants.LUKEWARM_OCEAN
                    if (secondValue == BiomeConstants.FROZEN_OCEAN) return BiomeConstants.COLD_OCEAN
                }
            }
        }
        if (firstValue == BiomeConstants.DEEP_OCEAN) when (secondValue) {
            BiomeConstants.OCEAN -> return BiomeConstants.DEEP_OCEAN
            BiomeConstants.FROZEN_OCEAN -> return BiomeConstants.DEEP_FROZEN_OCEAN
            BiomeConstants.LUKEWARM_OCEAN -> return BiomeConstants.DEEP_LUKEWARM_OCEAN
            BiomeConstants.COLD_OCEAN -> return BiomeConstants.DEEP_COLD_OCEAN
        }
        return secondValue
    }
}
