/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.registry

/**
 * Represents a registry, which is a bi-map of Int to T and T to Int
 */
interface Registry<T> : Iterable<T> {

    /**
     * Get the id for the specified [value], or -1 if the specified value is not registered
     *
     * @param value the value
     * @return the ID of the [value], or -1 if the value isn't registered
     */
    fun idOf(value: T): Int

    /**
     * Get the value for the specified [id], or null if there is no value with the specified [id]
     *
     * @param id the ID
     * @return the value with this [id], or null if there is no value with this [id]
     */
    operator fun get(id: Int): T?
}
