/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

/**
 * A bi-map of Int <-> T
 */
interface IdMap<T> : Iterable<T> {

    /**
     * Gets the id for the specified [value], or -1 if the specified value is not in this map
     *
     * @param value the value
     * @return the ID of the [value], or -1 if the value isn't registered
     */
    fun idOf(value: T): Int

    /**
     * Gets the value for the specified [id], or null if there is no value with the specified [id]
     *
     * @param id the ID
     * @return the value with this [id], or null if there is no value with this [id]
     */
    operator fun get(id: Int): T?
}
