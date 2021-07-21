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
package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import kotlin.random.Random

/**
 * Represents a tag that holds a list of values.
 *
 * @param T the type of the tag
 */
interface Tag<T : Any> : Keyed {

    /**
     * The name of this tag.
     */
    val name: Key

    /**
     * The type of this tag.
     */
    val type: TagType<T>

    /**
     * The values of this tag.
     */
    val values: List<T>

    /**
     * Returns true if this tag contains the specified [value], false otherwise.
     *
     * @param value the value
     * @return true if present, false otherwise
     */
    operator fun contains(value: T): Boolean

    /**
     * Gets a random value from the values of this tag using the given [random]
     * to generate the random index.
     *
     * @param random the random to use
     * @return a random value from this tag
     */
    fun random(random: Random) = values.run { this[random.nextInt(size)] }

    /**
     * Gets a random value from the values of this tag using [Random.Default] to
     * generate the random index.
     *
     * @return a random value from this tag
     */
    fun random() = random(Random.Default)

    override fun key() = name
}
