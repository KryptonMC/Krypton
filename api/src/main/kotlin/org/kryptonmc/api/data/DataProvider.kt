/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A provider that can be used to retrieve values of type [E] from arbitrary
 * data holders.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DataProvider<E> {

    /**
     * The key that this provider provides values for.
     */
    @get:JvmName("key")
    public val key: Key<E>

    /**
     * Checks if the [key] this provider is for is supported on the given data
     * [holder].
     *
     * @param holder the holder
     * @return true if supported, false otherwise
     */
    public fun isSupported(holder: DataHolder): Boolean

    /**
     * Gets the value associated with the [key] this provider is for on the
     * given [holder], or returns null if there is no value associated with
     * the key.
     *
     * @param holder the holder
     * @return the value, or null if not present
     */
    public operator fun get(holder: DataHolder): E?

    /**
     * Sets the value associated with the [key] this provider is for on the
     * given [holder] to the given [value].
     *
     * @param holder the holder
     * @param value the value
     */
    public fun set(holder: MutableDataHolder, value: E)

    /**
     * Removes the value associated with the [key] this provider is for from
     * the given [holder].
     *
     * @param holder the holder
     */
    public fun remove(holder: MutableDataHolder)

    /**
     * Creates a new data holder from the given [holder] with the [key] this
     * provider is for set to the given [value].
     *
     * @param holder the holder
     * @param value the value
     * @return a new data holder
     */
    public fun <I : ImmutableDataHolder<I>> set(holder: I, value: E): I

    /**
     * Creates a new data holder from the given [holder] with the value
     * associated with the [key] this provider is for removed.
     *
     * @param holder the holder
     * @return a new data holder
     */
    public fun <I : ImmutableDataHolder<I>> remove(holder: I): I

    /**
     * A builder containing common functions between mutable and immutable
     * builders.
     */
    public interface BaseBuilder<B : BaseBuilder<B, H, E>, H : DataHolder, E> : AbstractBuilder<DataProvider<E>> {

        /**
         * Sets the supports function for the data provider to the given
         * [supports] function.
         *
         * The supports function is used to determine if a data holder is
         * supported by the data provider. This is in excess to checks done to
         * verify that the types match, as the holder given to the
         * [DataProvider.isSupported] function must be of type [H], else that
         * function will always return false, regardless of whether this
         * function is set.
         *
         * @param supports the supports function
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun supports(supports: Predicate<H>): B

        /**
         * Sets the getter function for the data provider to the given
         * [getter] function.
         *
         * The getter function is used to get the value from a data holder. If
         * a getter function is not supplied, [DataProvider.get] will always
         * return null.
         *
         * @param getter the getter function
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun get(getter: Function<H, E?>): B
    }

    /**
     * A builder for building a data provider for mutable data holders.
     */
    public interface MutableBuilder<H : MutableDataHolder, E> : BaseBuilder<MutableBuilder<H, E>, H, E> {

        /**
         * Sets the setter function for the data provider to the given
         * [setter] function.
         *
         * The setter function is used to set a value on a data holder. If a
         * setter function is not supplied, [DataProvider.set] will always do
         * nothing.
         *
         * @param setter the setter function
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun set(setter: BiConsumer<H, E>): MutableBuilder<H, E>

        /**
         * Sets the removal function for the data provider to the given
         * [remover] function.
         *
         * The removal function is used to remove a value on a data holder. If
         * a removal function is not supplied, [DataProvider.remove] will
         * always do nothing.
         *
         * @param remover the removal function
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun remove(remover: Consumer<H>): MutableBuilder<H, E>
    }

    /**
     * A builder for building a data provider for immutable data holders.
     */
    public interface ImmutableBuilder<H : ImmutableDataHolder<H>, E> : BaseBuilder<ImmutableBuilder<H, E>, H, E> {

        /**
         * Sets the setter function for the data provider to the given
         * [setter] function.
         *
         * The setter function is used to set a value on a data holder. If a
         * setter function is not supplied, [DataProvider.set] will always
         * return the provided data holder.
         *
         * @param setter the setter function
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun set(setter: BiFunction<H, E, H>): ImmutableBuilder<H, E>

        /**
         * Sets the removal function for the data provider to the given
         * [remover] function.
         *
         * The removal function is used to remove a value on a data holder. If
         * a removal function is not supplied, [DataProvider.remove] will
         * always return the provided data holder.
         *
         * @param remover the removal function
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun remove(remover: Function<H, H>): ImmutableBuilder<H, E>
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun <H : MutableDataHolder, E> mutableBuilder(key: Key<E>, holderType: Class<H>): MutableBuilder<H, E>

        public fun <H : ImmutableDataHolder<H>, E> immutableBuilder(key: Key<E>, holderType: Class<H>): ImmutableBuilder<H, E>
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a mutable data provider for
         * providing and manipulating values for the given [key] on data
         * holders of the given [holderType].
         *
         * @param key the key
         * @param holderType the supported data holder type
         * @return a new mutable data provider builder
         */
        @JvmStatic
        public fun <H : MutableDataHolder, E> mutableBuilder(key: Key<E>, holderType: Class<H>): MutableBuilder<H, E> =
            FACTORY.mutableBuilder(key, holderType)

        /**
         * Creates a new builder for building an immutable data provider for
         * providing and manipulating values for the given [key] on data
         * holders of the given [holderType].
         *
         * @param key the key
         * @param holderType the supported data holder type
         * @return a new immutable data provider builder
         */
        @JvmStatic
        public fun <H : ImmutableDataHolder<H>, E> immutableBuilder(key: Key<E>, holderType: Class<H>): ImmutableBuilder<H, E> =
            FACTORY.immutableBuilder(key, holderType)
    }
}

/**
 * Creates a new builder for building a mutable data provider for
 * providing and manipulating values for the given [key] on data
 * holders of the given type [H].
 *
 * @param key the key
 * @return a new mutable data provider builder
 */
@JvmSynthetic
public inline fun <reified H : MutableDataHolder, E> DataProvider.Companion.mutableBuilder(key: Key<E>): DataProvider.MutableBuilder<H, E> =
    mutableBuilder(key, H::class.java)

/**
 * Creates a new builder for building an immutable data provider for
 * providing and manipulating values for the given [key] on data
 * holders of the given type [H].
 *
 * @param key the key
 * @return a new immutable data provider builder
 */
@JvmSynthetic
public inline fun <reified H : ImmutableDataHolder<H>, E> DataProvider.Companion.immutableBuilder(key: Key<E>): DataProvider.ImmutableBuilder<H, E> =
    immutableBuilder(key, H::class.java)
