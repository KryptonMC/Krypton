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

        override fun invoke(context: Context, n: Int, e: Int, s: Int, w: Int, center: Int): Int =
            if (center != 1 || n != 3 && e != 3 && s != 3 && w != 3 && n != 4 && e != 4 && s != 4 && w != 4) center else 2
    }

    object HeatIce : CastleTransformer {

        override fun invoke(context: Context, n: Int, e: Int, s: Int, w: Int, center: Int): Int =
            if (center != 4 || n != 1 && e != 1 && s != 1 && w != 1 && n != 2 && e != 2 && s != 2 && w != 2) center else 3
    }

    object IntroduceSpecial : C0Transformer {

        override fun invoke(context: Context, value: Int): Int {
            var temp = value
            if (!temp.isShallowOcean() && context.nextRandom(13) == 0) temp = temp or (1 + context.nextRandom(15) shl 8 and 3840)
            return temp
        }
    }
}
