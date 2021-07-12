package org.kryptonmc.api.block

import org.kryptonmc.api.space.Direction

enum class BlockFace(val direction: Direction) {

    BOTTOM(Direction.DOWN),
    TOP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    val opposite by lazy {
        when (this) {
            BOTTOM -> TOP
            TOP -> BOTTOM
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
        }
    }
}
