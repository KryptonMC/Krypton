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
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A dynamic reference to a value in a registry.
 *
 * This differs from [RegistryReference] in that the value may be in any
 * registry, or none at all, and retrieving the value requires the holder
 * to look in.
 */
public interface DynamicRegistryReference<T> : Keyed {

    /**
     * The key the value is mapped to.
     */
    public val key: ResourceKey<T>

    /**
     * Gets the value this reference points to in the registry held by the
     * given [holder].
     *
     * @param holder the holder containing the registry
     * @return the referenced value
     * @throws IllegalArgumentException if the value could not be found in the
     * holder
     */
    public fun get(holder: RegistryHolder): T

    override fun key(): Key = key.location

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun <T> of(registry: ResourceKey<out Registry<T>>, key: Key): DynamicRegistryReference<T>
    }

    public companion object {

        @JvmSynthetic
        internal fun <T> of(registry: ResourceKey<out Registry<T>>, key: Key): DynamicRegistryReference<T> {
            return Krypton.factory<Factory>().of(registry, key)
        }
    }
}
