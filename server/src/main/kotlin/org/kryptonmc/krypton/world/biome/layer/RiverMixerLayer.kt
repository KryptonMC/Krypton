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

object RiverMixerLayer : AreaTransformer2, DimensionOffset0Transformer {

    override fun invoke(context: Context, firstParent: Area, secondParent: Area, x: Int, z: Int): Int {
        val firstValue = firstParent[parentX(x), parentZ(z)]
        val secondValue = secondParent[parentX(x), parentZ(z)]
        if (firstValue.isOcean()) return firstValue
        if (secondValue == BiomeConstants.RIVER) return when (secondValue) {
            BiomeConstants.SNOWY_TUNDRA -> BiomeConstants.FROZEN_RIVER
            BiomeConstants.MUSHROOM_FIELDS, BiomeConstants.MUSHROOM_FIELD_SHORE -> BiomeConstants.MUSHROOM_FIELD_SHORE
            else -> secondValue and 255
        }
        return firstValue
    }
}
