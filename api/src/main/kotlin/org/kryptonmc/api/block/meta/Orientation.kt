/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.util.StringSerializable

/**
 * The orientation of a block.
 *
 * @param top the top direction
 * @param front the front direction
 */
public enum class Orientation(
    @get:JvmName("serialized") override val serialized: String,
    @get:JvmName("top") public val top: Direction,
    @get:JvmName("front") public val front: Direction
) : StringSerializable {

    UP_NORTH("up_north", Direction.UP, Direction.NORTH),
    UP_SOUTH("up_south", Direction.UP, Direction.SOUTH),
    UP_EAST("up_east", Direction.UP, Direction.EAST),
    UP_WEST("up_west", Direction.UP, Direction.WEST),
    DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH),
    DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH),
    DOWN_EAST("down_east", Direction.DOWN, Direction.EAST),
    DOWN_WEST("down_west", Direction.DOWN, Direction.WEST),
    NORTH_UP("north_up", Direction.NORTH, Direction.UP),
    SOUTH_UP("south_up", Direction.SOUTH, Direction.UP),
    EAST_UP("east_up", Direction.EAST, Direction.UP),
    WEST_UP("west_up", Direction.WEST, Direction.UP)
}
