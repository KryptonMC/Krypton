/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.resource.ResourceKey

/**
 * The manager for registries.
 */
public interface RegistryManager {

    /**
     * Gets the existing registry with the given resource [key], or returns
     * null if there is no existing registry with the given resource [key].
     *
     * @param key the key
     * @return the existing registry, or null if not present
     */
    public fun <T> registry(key: ResourceKey<out Registry<T>>): Registry<T>?

    /**
     * Gets the existing defaulted registry with the given resource [key], or
     * returns null if there is no existing defaulted registry with the given
     * resource [key].
     *
     * @param key the key
     * @return the existing defaulted registry, or null if not present
     */
    public fun <T> defaulted(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>?

    /**
     * Creates a new registry with the given registry [key].
     *
     * @param key the registry key
     * @return a registry for the given [key]
     */
    @Contract("_ -> new", pure = true)
    public fun <T> create(key: ResourceKey<out Registry<T>>): Registry<T>

    /**
     * Creates a new registry with the given registry [key], with a
     * [defaultKey].
     *
     * @param key the registry key
     * @param defaultKey the default key
     * @return a defaulted registry for the given [key]
     */
    @Contract("_, _ -> new", pure = true)
    public fun <T> createDefaulted(key: ResourceKey<out Registry<T>>, defaultKey: Key): DefaultedRegistry<T>
}
