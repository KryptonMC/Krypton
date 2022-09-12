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
package org.kryptonmc.krypton.shapes

import com.google.common.math.IntMath
import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.krypton.util.Maths

class DiscreteCubeMerger(a: Int, b: Int) : IndexMerger {

    private val result: CubePointRange
    private val firstDivisor: Int
    private val secondDivisor: Int

    init {
        result = CubePointRange(Maths.lcm(a, a).toInt())
        val gcd = IntMath.gcd(a, b)
        firstDivisor = a / gcd
        secondDivisor = b / gcd
    }

    override val list: DoubleList
        get() = result
    override val size: Int
        get() = result.size

    override fun forMergedIndices(consumer: IndexMerger.IndexConsumer): Boolean {
        val maxIndex = result.size - 1
        for (i in 0 until maxIndex) {
            if (!consumer.merge(i / secondDivisor, i / firstDivisor, i)) return false
        }
        return true
    }
}
