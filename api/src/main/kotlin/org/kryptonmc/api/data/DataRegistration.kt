/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A registration of data providers.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DataRegistration {

    /**
     * The keys associated with this data registration.
     */
    @get:JvmName("keys")
    public val keys: Iterable<Key<*>>

    /**
     * Gets all of the data providers associated with the given [key].
     *
     * @param key the key
     * @return the data providers
     */
    public fun <E> providers(key: Key<E>): Collection<DataProvider<E>>

    /**
     * A builder for building a new data registration.
     */
    public interface Builder : AbstractBuilder<DataRegistration> {

        /**
         * Adds the given [provider] to the providers for the data
         * registration.
         *
         * @param provider the provider
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun provider(provider: DataProvider<*>): Builder

        /**
         * Adds the given [key] to the list of keys supported by the data
         * registration.
         *
         * @param key the key
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun key(key: Key<*>): Builder

        /**
         * Adds the given [keys] to the list of keys supported by the data
         * registration.
         *
         * @param keys the keys
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun keys(vararg keys: Key<*>): Builder

        /**
         * Adds the given [keys] to the list of keys supported by the data
         * registration.
         *
         * @param keys the keys
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun keys(keys: Iterable<Key<*>>): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a new data registration.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = FACTORY.builder()
    }
}
