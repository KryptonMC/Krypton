/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.util.Direction

/**
 * Represents the face of a block.
 *
 * @param direction the direction this block face corresponds to
 */
public enum class BlockFace(@get:JvmName("direction") public val direction: Direction) {

    TOP(Direction.UP),
    BOTTOM(Direction.DOWN),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST);

    /**
     * The opposite of this face.
     */
    @get:JvmName("opposite")
    public val opposite: BlockFace by lazy {
        when (this) {
            TOP -> BOTTOM
            BOTTOM -> TOP
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }
}
