/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed

/**
 * The canvas of a painting.
 */
interface Canvas : Keyed {

    /**
     * The key of this motive.
     */
    val key: Key

    /**
     * The width of this motive.
     */
    val width: Int

    /**
     * The height of this motive.
     */
    val height: Int

    override fun key() = key
}
