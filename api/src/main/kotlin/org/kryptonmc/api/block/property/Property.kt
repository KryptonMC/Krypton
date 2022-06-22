/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.block.property

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * Represents a property key.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Properties::class)
public interface Property<T : Comparable<T>> {

    /**
     * The name of the property key.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The type of this property key.
     */
    @get:JvmName("type")
    public val type: Class<T>

    /**
     * The set of values this property key allows.
     */
    @get:JvmName("values")
    public val values: Set<T>

    /**
     * Parses the given string [value] to a value this property key allows,
     * or returns null if the value does not parse to [T].
     *
     * @param value the string value
     * @return the parsed value, or null if the value cannot be parsed to [T]
     */
    public fun fromString(value: String): T?

    /**
     * Converts the given [value] to its string equivalent.
     *
     * @param value the value
     * @return the string equivalent of the value
     */
    public fun toString(value: T): String

    @ApiStatus.Internal
    public interface Factory {

        public fun forBoolean(name: String): Property<Boolean>

        public fun forInt(name: String, values: Set<Int>): Property<Int>

        public fun <E : Enum<E>> forEnum(name: String, type: Class<E>, values: Set<E>): Property<E>
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new boolean property with the given [name].
         *
         * The accepted values for this property are always `true` and `false.
         *
         * @param name the name
         * @return a new boolean property
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun forBoolean(name: String): Property<Boolean> = FACTORY.forBoolean(name)

        /**
         * Creates a new integer property with the given [name] and the given
         * set of accepted [values].
         *
         * @param name the name
         * @param values the set of accepted values
         * @return a new integer property
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun forInt(name: String, values: Set<Int>): Property<Int> = FACTORY.forInt(name, values)

        /**
         * Creates a new integer property with the given [name] and the given
         * vararg array of accepted [values].
         *
         * @param name the name
         * @param values the array of accepted values
         * @return a new integer property
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun forInt(name: String, vararg values: Int): Property<Int> = forInt(name, values.toSet())

        /**
         * Creates a new integer property with the given [name] and the given
         * [range] of accepted values.
         *
         * @param name the name
         * @param range the range of accepted values
         * @return a new integer property
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun forInt(name: String, range: IntRange): Property<Int> = forInt(name, range.toSet())

        /**
         * Creates a new enum property with the given [name], [type], and the
         * given set of accepted [values].
         *
         * Enum properties use the lowercase name of the constant as the value
         * for the property.
         *
         * @param name the name
         * @param type the enum class
         * @param values the set of accepted values
         * @return a new enum property
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <E : Enum<E>> forEnum(name: String, type: Class<E>, values: Set<E>): Property<E> = FACTORY.forEnum(name, type, values)

        /**
         * Creates a new enum property with the given [name], [type], and the given
         * array of accepted [values].
         *
         * Enum properties use the lowercase name of the constant as the value
         * for the property.
         *
         * @param name the name
         * @param type the enum class
         * @param values the array of accepted values
         * @return a new enum property
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <E : Enum<E>> forEnum(name: String, type: Class<E>, values: Array<E>): Property<E> = forEnum(name, type, values.toSet())
    }
}

/**
 * Creates a new enum property with the given [name] and the given set
 * of accepted [values].
 *
 * @param name the name
 * @param values the set of accepted values
 * @return a new enum property
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun <reified E : Enum<E>> Property.Companion.forEnum(name: String, values: Set<E>): Property<E> = forEnum(name, E::class.java, values)

/**
 * Creates a new enum property with the given [name] and the given array
 * of accepted [values].
 *
 * @param name the name
 * @param values the array of accepted values
 * @return a new enum property
 */
@JvmSynthetic
@Contract("_ -> new", pure = true)
public inline fun <reified E : Enum<E>> Property.Companion.forEnum(name: String, values: Array<E>): Property<E> =
    forEnum(name, E::class.java, values)
