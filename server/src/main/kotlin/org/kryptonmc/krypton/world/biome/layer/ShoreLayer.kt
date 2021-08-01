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

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import org.kryptonmc.krypton.world.biome.BiomeConstants
import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.CastleTransformer

object ShoreLayer : CastleTransformer {

    private val SNOWY = IntOpenHashSet(intArrayOf(26, 11, 12, 13, 140, 30, 31, 158, 10))
    private val JUNGLES = IntOpenHashSet(intArrayOf(168, 169, 21, 22, 23, 149, 151))

    override fun invoke(context: Context, n: Int, e: Int, s: Int, w: Int, center: Int): Int {
        if (center == BiomeConstants.MUSHROOM_FIELDS) {
            if (n.isShallowOcean() || e.isShallowOcean() || s.isShallowOcean() || w.isShallowOcean()) return BiomeConstants.MUSHROOM_FIELD_SHORE
        } else if (JUNGLES.contains(center)) {
            if (!n.isJungleCompatible() || !e.isJungleCompatible() || !s.isJungleCompatible() || !w.isJungleCompatible()) return BiomeConstants.JUNGLE_EDGE
            if (n.isOcean() || e.isOcean() || s.isOcean() || w.isOcean()) return BiomeConstants.BEACH
        } else if (center != BiomeConstants.MOUNTAINS && center != BiomeConstants.WOODED_MOUNTAINS && center != BiomeConstants.MOUNTAIN_EDGE) {
            if (SNOWY.contains(center)) {
                if (!center.isOcean() && (n.isOcean() || e.isOcean() || s.isOcean() || w.isOcean())) return BiomeConstants.SNOWY_BEACH
            } else if (center != BiomeConstants.BADLANDS && center != BiomeConstants.WOODED_BADLANDS_PLATEAU) {
                if (!center.isOcean() && center != BiomeConstants.RIVER && center != BiomeConstants.SWAMP && (n.isOcean() || e.isOcean() || s.isOcean() || w.isOcean())) return BiomeConstants.BEACH
            } else if (!n.isOcean() && !e.isOcean() && !s.isOcean() && !w.isOcean() && (!n.isMesa() || !e.isMesa() || !s.isMesa() || !w.isMesa())) return BiomeConstants.DESERT
        } else if (!center.isOcean() && (n.isOcean() || e.isOcean() || s.isOcean() || w.isOcean())) return BiomeConstants.STONE_SHORE
        return center
    }

    private fun Int.isJungleCompatible() = JUNGLES.contains(this) || this == BiomeConstants.FOREST || this == BiomeConstants.TAIGA || isOcean()

    private fun Int.isMesa() = this == BiomeConstants.BADLANDS ||
            this == BiomeConstants.WOODED_BADLANDS_PLATEAU ||
            this == BiomeConstants.BADLANDS_PLATEAU ||
            this == BiomeConstants.ERODED_BADLANDS ||
            this == BiomeConstants.MODIFIED_WOODED_BADLANDS_PLATEAU ||
            this == BiomeConstants.MODIFIED_BADLANDS_PLATEAU
}
