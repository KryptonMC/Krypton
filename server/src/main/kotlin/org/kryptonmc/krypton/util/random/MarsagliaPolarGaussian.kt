/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
