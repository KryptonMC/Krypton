/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of a statistic.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(StatisticTypes::class)
public interface StatisticType<T : Any> : Iterable<Statistic<T>>, Keyed {

    /**
     * All of the statistics for this type.
     */
    @get:JvmName("statistics")
    public val statistics: Map<T, Statistic<T>>

    /**
     * The registry for this statistic type.
     */
    @get:JvmName("registry")
    public val registry: Registry<T>

    /**
     * Returns true if this type contains a statistic for the given [key],
     * false otherwise.
     *
     * @param key the key
     * @return true if this type contains a statistic for the key, false
     * otherwise
     */
    public operator fun contains(key: T): Boolean

    /**
     * Gets the statistic for the given [key], creating it if it does not
     * already exist.
     *
     * @param key the key
     * @return the statistic for the key
     */
    public operator fun get(key: T): Statistic<T>

    /**
     * Gets the statistic for the given [key] with the given [formatter],
     * creating it if it does not already exist.
     *
     * @param key the key
     * @param formatter the formatter
     * @return the statistic for the key
     */
    public operator fun get(key: T, formatter: StatisticFormatter): Statistic<T>

    @ApiStatus.Internal
    public interface Factory {

        public fun <T : Any> of(key: Key, registry: Registry<T>): StatisticType<T>
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new statistic type with the given values.
         *
         * @param key the key
         * @param registry the registry
         * @return a new statistic type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <T : Any> of(key: Key, registry: Registry<T>): StatisticType<T> = FACTORY.of(key, registry)
    }
}
