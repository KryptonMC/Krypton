/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

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
