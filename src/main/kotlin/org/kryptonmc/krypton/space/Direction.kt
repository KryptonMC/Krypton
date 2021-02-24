package org.kryptonmc.krypton.space

import org.kryptonmc.krypton.world.block.BlockState

enum class Direction(override val value: Int) : BlockState<Int> {

    UP(1),
    DOWN(0),
    NORTH(2),
    SOUTH(3),
    EAST(5),
    WEST(4)
}