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
