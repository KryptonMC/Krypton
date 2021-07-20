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
import org.kryptonmc.api.registry.RegistryRoots

/**
 * A key pointing to some form of resources.
 *
 * @param registry the [Key] name of the parent registry
 * @param location the [Key] location of the registry
 * @param T the type of this key
 */
data class ResourceKey<T : Any>(val registry: Key, val location: Key) {

    /**
     * Creates a new resource key with the given [parent] registry key, and the given [location]
     *
     * @param parent the parent key
     * @param location the location
     * @return a new resource key
     */
    constructor(parent: ResourceKey<out Registry<T>>, location: Key) : this(parent.location, location)

    /**
     * Creates a new resource key with the [minecraft][RegistryRoots.MINECRAFT] root as its root
     * key, and the given [location].
     *
     * @param location the location
     * @return a new resource key
     */
    constructor(location: Key) : this(RegistryRoots.MINECRAFT, location)
}
