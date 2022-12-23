/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import org.kryptonmc.api.resource.ResourceKey

/**
 * The manager for registries.
 */
public interface RegistryManager {

    /**
     * Gets the existing registry with the given resource [key], or returns
     * null if there is no existing registry with the given resource [key].
     *
     * @param T the registry element type
     * @param key the key
     * @return the existing registry, or null if not present
     */
    public fun <T> getRegistry(key: ResourceKey<out Registry<T>>): Registry<T>?

    /**
     * Gets the existing defaulted registry with the given resource [key], or
     * returns null if there is no existing defaulted registry with the given
     * resource [key].
     *
     * @param T the registry element type
     * @param key the key
     * @return the existing defaulted registry, or null if not present
     */
    public fun <T> getDefaultedRegistry(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>?
}
