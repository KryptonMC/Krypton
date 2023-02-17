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
 * Something that holds registries.
 */
public interface RegistryHolder {

    /**
     * All the registries contained within this registry holder.
     */
    public val registries: Collection<Registry<*>>

    /**
     * Gets a registry from this registry holder with the given [key].
     *
     * @param E the registry type
     * @param key the registry key
     * @return the registry, if present
     */
    public fun <E> getRegistry(key: ResourceKey<out Registry<E>>): Registry<E>?

    /**
     * Gets a defaulted registry from this registry holder with the
     * given [key].
     *
     * @param E the registry type
     * @param key the registry key
     * @return the defaulted registry, if present
     */
    public fun <E> getDefaultedRegistry(key: ResourceKey<out Registry<E>>): DefaultedRegistry<E>?
}
