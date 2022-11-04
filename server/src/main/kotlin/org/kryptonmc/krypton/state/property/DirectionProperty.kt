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

import com.google.common.collect.Sets
import org.kryptonmc.api.util.Direction
import java.util.Arrays
import java.util.EnumSet
import java.util.function.Predicate

class DirectionProperty(name: String, values: Collection<Direction>) : EnumProperty<Direction>(name, Direction::class.java, values) {

    companion object {

        private val VALUES = Direction.values()
        private val VALUE_SET = Sets.immutableEnumSet(EnumSet.allOf(Direction::class.java))

        @JvmStatic
        fun create(name: String): DirectionProperty = DirectionProperty(name, VALUE_SET)

        @JvmStatic
        fun create(name: String, predicate: Predicate<Direction>): DirectionProperty =
            DirectionProperty(name, Arrays.stream(VALUES).filter(predicate).collect(Sets.toImmutableEnumSet()))

        @JvmStatic
        fun create(name: String, vararg values: Direction): DirectionProperty = create(name, values.asList())

        @JvmStatic
        private fun create(name: String, values: Collection<Direction>): DirectionProperty = DirectionProperty(name, Sets.immutableEnumSet(values))
    }
}
