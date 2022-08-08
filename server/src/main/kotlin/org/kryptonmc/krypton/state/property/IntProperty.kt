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
package org.kryptonmc.krypton.state.property

import it.unimi.dsi.fastutil.ints.IntArraySet
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

class IntProperty(name: String, private val minimum: Int, private val maximum: Int) : KryptonProperty<Int>(name, Int::class.java) {

    override val values: ImmutableSet<Int>

    init {
        require(minimum >= 0) { "Minimum value $minimum of $name must be 0 or greater!" }
        require(maximum > minimum) { "Maximum value $maximum of $name must be greater than minimum value $minimum!" }
        val valueSet = IntArraySet()
        for (i in minimum..maximum) {
            valueSet.add(i)
        }
        values = valueSet.toImmutableSet()
    }

    override fun idFor(value: Int): Int {
        val result = value - minimum
        return result or ((maximum - result) shr 31)
    }

    override fun fromString(value: String): Int? = value.toIntOrNull()

    override fun generateHashCode(): Int = 31 * super.generateHashCode() + values.hashCode()
}
