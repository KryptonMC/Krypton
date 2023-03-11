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

import it.unimi.dsi.fastutil.doubles.DoubleList

class NonOverlappingMerger(
    private val lower: DoubleList,
    private val upper: DoubleList,
    private val swap: Boolean
) : AbstractDoubleListIndexMerger() {

    override val size: Int
        get() = lower.size + upper.size

    override fun asList(): DoubleList = this

    override fun forMergedIndices(consumer: IndexMerger.IndexConsumer): Boolean {
        if (swap) return forNonSwappedIndices { x, y, z -> consumer.merge(y, x, z) }
        return forNonSwappedIndices(consumer)
    }

    override fun getDouble(index: Int): Double {
        if (index < lower.size) return lower.getDouble(index)
        return upper.getDouble(index - lower.size)
    }

    private fun forNonSwappedIndices(consumer: IndexMerger.IndexConsumer): Boolean {
        val lowerCount = lower.size
        for (i in 0 until lowerCount) {
            if (!consumer.merge(i, -1, i)) return false
        }
        val maxUpperIndex = upper.size - 1
        for (i in 0 until maxUpperIndex) {
            if (!consumer.merge(lowerCount - 1, i, lowerCount + i)) return false
        }
        return true
    }
}
