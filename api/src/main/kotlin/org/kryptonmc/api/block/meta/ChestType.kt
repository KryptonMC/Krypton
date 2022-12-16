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
 * Indicates the type of chest a block this property is applied to represents.
 *
 * This is used to help the client properly attach chest models, and combine
 * adjacent chest blocks where necessary, for example, with the double chest,
 * where adjacent chests need to be attached across blocks.
 */
public enum class ChestType(private val oppositeId: Int) {

    /**
     * A single chest that is not connected to any other chest.
     */
    SINGLE(0),

    /**
     * The left half of a double chest.
     */
    LEFT(2),

    /**
     * The right half of a double chest.
     */
    RIGHT(1);

    /**
     * Gets the chest type opposite to this chest type.
     *
     * If this chest is the left half of a double chest, the opposite will be
     * the right half, and if it is the right half, the opposite will be the
     * left half. If this chest is a single chest, it has no opposite type,
     * and so the opposite will just be single.
     *
     * @return the opposite chest type
     */
    public fun opposite(): ChestType = BY_ID[oppositeId]

    public companion object {

        private val BY_ID = values()
    }
}
