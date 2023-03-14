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
package org.kryptonmc.krypton.util.math

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.api.util.Direction

enum class FrontAndTop(val front: Direction, val top: Direction) {

    DOWN_EAST(Direction.DOWN, Direction.EAST),
    DOWN_NORTH(Direction.DOWN, Direction.NORTH),
    DOWN_SOUTH(Direction.DOWN, Direction.SOUTH),
    DOWN_WEST(Direction.DOWN, Direction.WEST),
    UP_EAST(Direction.UP, Direction.EAST),
    UP_NORTH(Direction.UP, Direction.NORTH),
    UP_SOUTH(Direction.UP, Direction.SOUTH),
    UP_WEST(Direction.UP, Direction.WEST),
    WEST_UP(Direction.WEST, Direction.UP),
    EAST_UP(Direction.EAST, Direction.UP),
    NORTH_UP(Direction.NORTH, Direction.UP),
    SOUTH_UP(Direction.SOUTH, Direction.UP);

    companion object {

        private val LOOKUP_TOP_FRONT = Int2ObjectOpenHashMap<FrontAndTop>(values().size).apply {
            values().forEach { put(lookupKey(it.front, it.top), it) }
        }

        @JvmStatic
        fun fromFrontAndTop(front: Direction, top: Direction): FrontAndTop = LOOKUP_TOP_FRONT.get(lookupKey(front, top))

        @JvmStatic
        private fun lookupKey(front: Direction, top: Direction): Int = top.ordinal shl 3 or front.ordinal
    }
}
