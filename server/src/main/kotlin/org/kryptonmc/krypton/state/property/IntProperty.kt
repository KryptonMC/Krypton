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

import com.google.common.collect.ImmutableSet

class IntProperty(name: String, private val minimum: Int, private val maximum: Int) : KryptonProperty<Int>(name, Int::class.javaObjectType) {

    override val values: Collection<Int>

    init {
        require(minimum >= 0) { "Minimum value $minimum of $name must be 0 or greater!" }
        require(maximum > minimum) { "Maximum value $maximum of $name must be greater than minimum value $minimum!" }
        val valueSet = ImmutableSet.builder<Int>()
        for (i in minimum..maximum) {
            valueSet.add(i)
        }
        values = valueSet.build()
    }

    override fun fromString(value: String): Int? {
        return try {
            val integer = Integer.valueOf(value)
            if (integer >= minimum && integer <= maximum) integer else null
        } catch (exception: NumberFormatException) {
            null
        }
    }

    override fun toString(value: Int): String = value.toString()

    @Suppress("MagicNumber")
    override fun idFor(value: Int): Int {
        val result = value - minimum
        return result or (maximum - result shr 31)
    }

    @Suppress("MagicNumber") // This is a hash code function
    override fun generateHashCode(): Int = 31 * super.generateHashCode() + values.hashCode()
}
