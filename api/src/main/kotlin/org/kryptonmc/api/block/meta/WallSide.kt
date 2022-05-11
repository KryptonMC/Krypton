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
 * Indicates how a wall this property is applied to connects to an adjacent
 * wall or block on one of its faces.
 */
// TODO: Find out what low and tall are
public enum class WallSide {

    /**
     * The wall has no connection.
     */
    NONE,
    LOW,
    TALL
}
