/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.util.StringSerializable

/**
 * Represents a type of chest.
 */
public enum class ChestType(override val serialized: String) : StringSerializable {

    SINGLE("single"),
    LEFT("left"),
    RIGHT("right");

    /**
     * The chest type opposite to this chest type.
     */
    public val opposite: ChestType by lazy {
        when (this) {
            SINGLE -> SINGLE
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}
