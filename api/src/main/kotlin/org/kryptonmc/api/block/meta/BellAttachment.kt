/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

/**
 * Indicates how a bell block that this may be applied to attaches to the
 * block it was placed on.
 */
public enum class BellAttachment {

    /**
     * The bell is attached to the top face of the block below it.
     */
    FLOOR,

    /**
     * The bell is attached to the bottom face of the block above it.
     */
    CEILING,

    /**
     * The bell is attached to the north, south, east, or west face of a
     * single block to the north, south, east, or west of it.
     */
    SINGLE_WALL,

    /**
     * The bell is attached to the north/south or east/west face of two blocks
     * on opposing sides of the bell, either the north/south or east/west sides.
     */
    DOUBLE_WALL
}
