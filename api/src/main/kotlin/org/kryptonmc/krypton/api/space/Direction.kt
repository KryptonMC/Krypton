package org.kryptonmc.krypton.api.space

/**
 * Represents a three-dimensional
 * [Cardinal direction](https://en.wikipedia.org/wiki/Cardinal_direction).
 *
 * As this is three-dimensional, this also includes the directions [UP]
 * and [DOWN].
 *
 * [id] is the ID of this [Direction]. This should only need to be used internally.
 *
 * @author Callum Seabrook
 */
enum class Direction(val id: Int) {

    UP(1),
    DOWN(0),
    NORTH(2),
    SOUTH(3),
    EAST(5),
    WEST(4)
}