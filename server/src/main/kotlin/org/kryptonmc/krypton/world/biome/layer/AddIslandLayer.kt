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

import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.BishopTransformer

object AddIslandLayer : BishopTransformer {

    override fun invoke(context: Context, southWest: Int, southEast: Int, northEast: Int, northWest: Int, center: Int): Int {
        if (
            !center.isShallowOcean() ||
            northWest.isShallowOcean() &&
            northEast.isShallowOcean() &&
            southWest.isShallowOcean() &&
            southEast.isShallowOcean()
        ) {
            if (
                !center.isShallowOcean() &&
                (northWest.isShallowOcean() || southWest.isShallowOcean() ||
                        northEast.isShallowOcean() || southEast.isShallowOcean()) &&
                context.nextRandom(5) == 0
            ) {
                if (northWest.isShallowOcean()) return if (center == 4) 4 else northWest
                if (southWest.isShallowOcean()) return if (center == 4) 4 else southWest
                if (northEast.isShallowOcean()) return if (center == 4) 4 else northEast
                if (southEast.isShallowOcean()) return if (center == 4) 4 else southEast
            }
            return center
        }
        var i = 1
        var selected = 1
        if (!northWest.isShallowOcean() && context.nextRandom(i++) == 0) selected = northWest
        if (!northEast.isShallowOcean() && context.nextRandom(i++) == 0) selected = northEast
        if (!southWest.isShallowOcean() && context.nextRandom(i++) == 0) selected = southWest
        if (!southEast.isShallowOcean() && context.nextRandom(i + 1) == 0) selected = southEast
        return when {
            context.nextRandom(3) == 0 -> selected
            selected == 4 -> 4
            else -> selected
        }
    }
}
