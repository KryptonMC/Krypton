/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import io.leangen.geantyref.TypeToken
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A key that may be used to retrieve values of type [V] from data holders.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Key<out V> : Keyed {

    /**
     * The generic type representing the value type [V].
     */
    @get:JvmName("type")
    public val type: TypeToken<@UnsafeVariance V>

    @ApiStatus.Internal
    public interface Factory {

        public fun <V> of(key: net.kyori.adventure.key.Key, type: TypeToken<V>): Key<V>
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new key with the given [key] and [type].
         *
         * @param key the key
         * @param type the type
         * @return a new key
         */
        @JvmStatic
        public fun <V> of(key: net.kyori.adventure.key.Key, type: TypeToken<V>): Key<V> = FACTORY.of(key, type)

        /**
         * Creates a new key with the given [key] and [type].
         *
         * @param key the key
         * @param type the type
         * @return a new key
         */
        @JvmStatic
        public fun <V> of(key: net.kyori.adventure.key.Key, type: Class<V>): Key<V> = of(key, TypeToken.get(type))
    }
}

/**
 * Creates a new key with the given [key] and type [V].
 *
 * @param key the key
 * @return a new key
 */
public inline fun <reified V> Key.Companion.of(key: net.kyori.adventure.key.Key): Key<V> = of(key, object : TypeToken<V>() {})
