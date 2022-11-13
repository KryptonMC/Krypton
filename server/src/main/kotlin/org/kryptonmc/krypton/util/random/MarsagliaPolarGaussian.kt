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
package org.kryptonmc.krypton.util.random

import kotlin.math.ln
import kotlin.math.sqrt

class MarsagliaPolarGaussian(private val randomSource: RandomSource) {

    private var nextNextGaussian = 0.0
    private var haveNextNextGaussian = false

    fun reset() {
        haveNextNextGaussian = false
    }

    fun nextGaussian(): Double {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false
            return nextNextGaussian
        }
        var u: Double
        var v: Double
        var s: Double
        do {
            u = randomSource.nextDouble() * 2.0 - 1.0
            v = randomSource.nextDouble() * 2.0 - 1.0
            s = u * u + v * v
        } while (s >= 1.0 || s == 0.0)
        val result = sqrt(-2.0 * ln(s) / s)
        nextNextGaussian = v * s
        haveNextNextGaussian = true
        return u * result
    }
}
