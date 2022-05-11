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
 * Indicates which part of the two block bed a block this property is applied
 * to represents.
 *
 * The head of the bed is the part with the pillow, and where the player's
 * head is when the player lays down in the bed. The foot is the other end,
 * where the player's feet are when laying down.
 */
public enum class BedPart {

    HEAD,
    FOOT
}
