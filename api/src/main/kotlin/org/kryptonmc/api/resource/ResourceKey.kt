/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.resource

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import java.util.Collections
import java.util.IdentityHashMap

/**
 * A key pointing to some form of resources.
 *
 * @param registry the [Key] name of the parent registry
 * @param location the [Key] location of the registry
 * @param T the type of this key
 */
class ResourceKey<T : Any> private constructor(val registry: Key, val location: Key) {

    companion object {

        private val VALUES: MutableMap<String, ResourceKey<*>> = Collections.synchronizedMap(IdentityHashMap())

        /**
         * Creates a new resource key, or returns an existing one if one with the
         * given parameters has already been created, with the given [registry] as
         * its parent name, and the given [location] as the location of the resource.
         *
         * @param registry the parent registry name
         * @param location the location of the resource
         * @return a resource key
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> of(registry: Key, location: Key): ResourceKey<T> {
            val key = "$registry:$location".intern()
            return VALUES.getOrPut(key) { ResourceKey<T>(registry, location) } as ResourceKey<T>
        }

        /**
         * Creates a new resource key, or returns an existing one if one with the
         * given parameters has already been created, with the given [parent] as
         * its parent, and the given [location] as the location of the resource.
         *
         * @param parent the parent key
         * @param location the location of the resource
         * @return a resource key
         */
        fun <T : Any> of(parent: ResourceKey<out Registry<T>>, location: Key) = of<T>(parent.location, location)
    }
}
