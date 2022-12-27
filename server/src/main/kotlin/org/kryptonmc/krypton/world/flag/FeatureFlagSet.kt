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
package org.kryptonmc.krypton.world.flag

import it.unimi.dsi.fastutil.HashCommon

class FeatureFlagSet(private val universe: FeatureFlagUniverse?, private val mask: Long) {

    fun contains(flag: FeatureFlag): Boolean {
        if (universe != flag.universe) return false
        return mask and flag.mask != 0L
    }

    fun isSubsetOf(other: FeatureFlagSet): Boolean {
        if (universe == null) return true
        if (universe != other.universe) return false
        return mask and other.mask.inv() == 0L
    }

    fun join(other: FeatureFlagSet): FeatureFlagSet {
        if (universe == null) return other
        if (other.universe == null) return this
        require(universe == other.universe) { "Mismatched set elements: $universe != ${other.universe}!" }
        return FeatureFlagSet(universe, mask or other.mask)
    }

    override fun equals(other: Any?): Boolean = this === other || other is FeatureFlagSet && universe == other.universe && mask == other.mask

    override fun hashCode(): Int = HashCommon.mix(mask).toInt()

    companion object {

        private val EMPTY = FeatureFlagSet(null, 0L)

        @JvmStatic
        fun create(universe: FeatureFlagUniverse, flags: Collection<FeatureFlag>): FeatureFlagSet {
            if (flags.isEmpty()) return EMPTY
            return FeatureFlagSet(universe, computeMask(universe, 0L, flags))
        }

        @JvmStatic
        fun of(): FeatureFlagSet = EMPTY

        @JvmStatic
        fun of(flag: FeatureFlag): FeatureFlagSet = FeatureFlagSet(flag.universe, flag.mask)

        @JvmStatic
        fun of(first: FeatureFlag, vararg others: FeatureFlag): FeatureFlagSet {
            val mask = if (others.isEmpty()) first.mask else computeMask(first.universe, first.mask, others.asList())
            return FeatureFlagSet(first.universe, mask)
        }

        @JvmStatic
        private fun computeMask(universe: FeatureFlagUniverse, mask: Long, flags: Iterable<FeatureFlag>): Long {
            var temp = mask
            flags.forEach { flag ->
                if (universe != flag.universe) error("Mismatched feature universe! Expected $universe, got ${flag.universe}!")
                temp = temp or flag.mask
            }
            return temp
        }
    }
}
