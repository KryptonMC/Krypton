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
 * Indicates the face of a connected block that a block this property is
 * applied to is attached to.
 */
public enum class AttachFace {

    /**
     * The block is attached to the top face of the block below it.
     */
    FLOOR,

    /**
     * The block is attached to the north, south, east, or west face of the
     * block to the north, south, east, or west of it.
     */
    WALL,

    /**
     * The block is attached to the bottom face of the block above it.
     */
    CEILING
}
