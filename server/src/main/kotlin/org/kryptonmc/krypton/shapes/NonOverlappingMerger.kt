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

import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.krypton.util.AbstractDoubleListIndexMerger

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
