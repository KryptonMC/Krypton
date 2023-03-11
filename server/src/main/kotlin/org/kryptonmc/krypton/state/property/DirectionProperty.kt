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
