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
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.util.Direction

/**
 * Indicates the orientation of a block that may be oriented in two directions
 * on two separate axes, such as a jigsaw block.
 *
 * @property top The top part of the direction.
 * @property front The front part of the direction.
 */
public enum class Orientation(public val top: Direction, public val front: Direction) {

    UP_NORTH(Direction.UP, Direction.NORTH),
    UP_SOUTH(Direction.UP, Direction.SOUTH),
    UP_EAST(Direction.UP, Direction.EAST),
    UP_WEST(Direction.UP, Direction.WEST),
    DOWN_NORTH(Direction.DOWN, Direction.NORTH),
    DOWN_SOUTH(Direction.DOWN, Direction.SOUTH),
    DOWN_EAST(Direction.DOWN, Direction.EAST),
    DOWN_WEST(Direction.DOWN, Direction.WEST),
    NORTH_UP(Direction.NORTH, Direction.UP),
    SOUTH_UP(Direction.SOUTH, Direction.UP),
    EAST_UP(Direction.EAST, Direction.UP),
    WEST_UP(Direction.WEST, Direction.UP)
}
