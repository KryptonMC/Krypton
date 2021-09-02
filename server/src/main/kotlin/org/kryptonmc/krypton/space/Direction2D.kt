package org.kryptonmc.krypton.space

import com.google.common.collect.Sets
import org.kryptonmc.api.space.Direction

enum class Direction2D(vararg directions: Direction) {

    NORTH(Direction.NORTH),
    NORTH_EAST(Direction.NORTH, Direction.EAST),
    EAST(Direction.EAST),
    SOUTH_EAST(Direction.SOUTH, Direction.EAST),
    SOUTH(Direction.SOUTH),
    SOUTH_WEST(Direction.SOUTH, Direction.WEST),
    WEST(Direction.WEST),
    NORTH_WEST(Direction.NORTH, Direction.WEST);

    val directions: Set<Direction> = Sets.immutableEnumSet(directions.toList())
}
