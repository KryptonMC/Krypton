/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import java.util.Collections
import java.util.IdentityHashMap

/**
 * A registry key for a [Registry].
 *
 * @param registryName the [Key] name of the parent registry
 * @param location the [Key] location of the registry
 * @param T the type of this key
 */
class RegistryKey<T>(val registryName: Key, val location: Key) {

    init {
        val string = "$registryName:$location".intern()
        VALUES.putIfAbsent(string, this)
    }

    override fun toString() = "RegistryKey(registryName=$registryName, location=$location)"

    companion object {

        private val VALUES = Collections.synchronizedMap(IdentityHashMap<String, RegistryKey<*>>())

        /**
         * Creates a new registry key with the given [parent] registry key, and the given [location]
         *
         * @param parent the parent key
         * @param location the location
         * @return a new registry key
         */
        fun <T> of(parent: RegistryKey<out Registry<T>>, location: Key) = RegistryKey<T>(parent.location, location)

        /**
         * Creates a new registry key with the [minecraft][RegistryRoots.MINECRAFT] root as its root
         * key, and the given [location].
         *
         * @param location the location
         * @return a new registry key
         */
        fun <T> of(location: Key): RegistryKey<Registry<T>> = RegistryKey(RegistryRoots.MINECRAFT, location)
    }
}
