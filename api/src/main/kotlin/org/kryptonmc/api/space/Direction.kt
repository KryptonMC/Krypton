/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.space

/**
 * Represents a three-dimensional
 * [Cardinal direction](https://en.wikipedia.org/wiki/Cardinal_direction).
 *
 * As this is three-dimensional, this also includes the directions [UP]
 * and [DOWN].
 *
 * [id] is the ID of this [Direction]. This should only need to be used internally.
 */
enum class Direction(val id: Int) {

    UP(1),
    DOWN(0),
    NORTH(2),
    SOUTH(3),
    EAST(5),
    WEST(4)
}
