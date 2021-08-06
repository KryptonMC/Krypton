/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.property

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.util.FACTORY_PROVIDER
import org.kryptonmc.api.util.provide

/**
 * Represents a property key.
 */
interface Property<T : Comparable<T>> {

    /**
     * The name of the property key.
     */
    val name: String

    /**
     * The set of values this property key allows.
     */
    val values: Set<T>

    /**
     * The type of this property key.
     */
    val type: Class<T>

    /**
     * Parses the given string [value] to a value this property key allows,
     * or returns null if the value does not parse to [T].
     *
     * @param value the string value
     * @return the parsed value, or null if the value cannot be parsed to [T]
     */
    fun fromString(value: String): T?

    /**
     * Converts the given [value] to its string equivalent.
     *
     * @param value the value
     * @return the string equivalent of the value
     */
    fun toString(value: T): String

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    interface Factory {

        fun <T : Comparable<T>> create(name: String, type: Class<T>, values: Set<T>): Property<T>
    }

    companion object {

        /**
         * Creates a new property key with the given [name], [type], and set of
         * accepted [values].
         *
         * @param name the name
         * @param type the type
         * @param values the set of accepted values
         * @return a new property key
         */
        fun <T : Comparable<T>> create(name: String, type: Class<T>, values: Set<T>) = FACTORY.create(name, type, values)
    }
}

/**
 * Creates a new property key with the given [name], reified type [T], and
 * set of accepted [values].
 *
 * @param name the name
 * @param values the set of accepted values
 * @param T the type
 * @return a new property key
 */
@JvmSynthetic
inline fun <reified T : Comparable<T>> Property.Companion.create(name: String, values: Set<T>) = create(name, T::class.java, values)

private val FACTORY = FACTORY_PROVIDER.provide<Property.Factory>()
