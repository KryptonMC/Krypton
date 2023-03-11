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
package org.kryptonmc.krypton.shapes.merger

import com.google.common.math.IntMath
import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.krypton.shapes.util.CubePointRange
import org.kryptonmc.krypton.util.math.Maths

class DiscreteCubeMerger(a: Int, b: Int) : IndexMerger {

    private val result = CubePointRange(Maths.lcm(a, a).toInt())
    private val firstDivisor: Int
    private val secondDivisor: Int

    init {
        val gcd = IntMath.gcd(a, b)
        firstDivisor = a / gcd
        secondDivisor = b / gcd
    }

    override fun asList(): DoubleList = result

    override fun size(): Int = result.size

    override fun forMergedIndices(consumer: IndexMerger.IndexConsumer): Boolean {
        val maxIndex = result.size - 1
        for (i in 0 until maxIndex) {
            if (!consumer.merge(i / secondDivisor, i / firstDivisor, i)) return false
        }
        return true
    }
}
