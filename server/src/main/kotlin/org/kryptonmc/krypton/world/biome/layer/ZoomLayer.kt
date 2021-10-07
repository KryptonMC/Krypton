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

import org.kryptonmc.krypton.world.biome.area.Area
import org.kryptonmc.krypton.world.biome.context.BigContext
import org.kryptonmc.krypton.world.biome.layer.traits.AreaTransformer1

enum class ZoomLayer : AreaTransformer1 {

    NORMAL,
    FUZZY {

        override fun modeOrRandom(context: BigContext<*>, a: Int, b: Int, c: Int, d: Int) = context.random(a, b, c, d)
    };

    protected open fun modeOrRandom(context: BigContext<*>, a: Int, b: Int, c: Int, d: Int): Int = when {
        b == c && c == d -> b
        (a == b && a == c) || (a == b && a == d) || (a == c && a == d) || (a == b && c != d) || (a == c && b != d) || (a == d && b != c) -> a
        (b == c && a != d) || (b == d && a != c) -> b
        c == d && a == b -> c
        else -> context.random(a, b, c, d)
    }

    override fun parentX(x: Int) = x shr 1

    override fun parentZ(z: Int) = z shr 1

    override fun invoke(context: BigContext<*>, parent: Area, x: Int, z: Int): Int {
        val first = parent[parentX(x), parentZ(z)]
        context.initRandom((x shr 1 shl 1).toLong(), (z shr 1 shl 1).toLong())
        val andX = x and 1
        val andZ = z and 1
        if (andX == 0 && andZ == 0) return first
        val second = parent[parentX(x), parentZ(z + 1)]
        val firstRandom = context.random(first, second)
        if (andX == 1 && andZ == 1) return firstRandom
        val third = parent[parentX(x + 1), parentZ(z)]
        val secondRandom = context.random(first, third)
        if (andX == 1 && andZ == 0) return secondRandom
        val fourth = parent[parentX(x + 1), parentZ(z + 1)]
        return modeOrRandom(context, first, third, second, fourth)
    }
}
