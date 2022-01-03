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

    BOTTOM(Direction.DOWN),
    TOP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    /**
     * The opposite of this face.
     *
     * Lazily computed to avoid circular dependencies.
     */
    @get:JvmName("opposite")
    public val opposite: BlockFace by lazy {
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
