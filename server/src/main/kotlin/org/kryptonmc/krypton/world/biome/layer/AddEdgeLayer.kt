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
import org.kryptonmc.krypton.world.biome.layer.traits.C0Transformer
import org.kryptonmc.krypton.world.biome.layer.traits.CastleTransformer

object AddEdgeLayer {

    object CoolWarm : CastleTransformer {

        override fun invoke(context: Context, north: Int, east: Int, south: Int, west: Int, center: Int): Int {
            if (
                center != 1 ||
                north != 3 && east != 3 && south != 3 && west != 3 &&
                north != 4 && east != 4 && south != 4 && west != 4
            ) {
                return center
            }
            return 2
        }
    }

    object HeatIce : CastleTransformer {

        override fun invoke(context: Context, north: Int, east: Int, south: Int, west: Int, center: Int): Int {
            if (
                center != 4 ||
                north != 1 && east != 1 && south != 1 && west != 1 &&
                north != 2 && east != 2 && south != 2 && west != 2
            ) {
                return center
            }
            return 3
        }
    }

    object IntroduceSpecial : C0Transformer {

        override fun invoke(context: Context, value: Int): Int {
            var temp = value
            if (!temp.isShallowOcean() && context.nextRandom(13) == 0) {
                temp = temp or (1 + context.nextRandom(15) shl 8 and 3840)
            }
            return temp
        }
    }
}
