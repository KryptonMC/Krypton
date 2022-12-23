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
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.function.Supplier

/**
 * A reference to a value in a registry.
 *
 * This allows underlying registry values to be modified dynamically without
 * needing to update all the old values.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface RegistryReference<T> : Supplier<T>, Keyed {

    /**
     * The key the value is mapped to.
     */
    @get:JvmName("key")
    public val key: ResourceKey<T>

    /**
     * Gets the value this reference points to in the registry.
     *
     * @return the referenced value
     */
    override fun get(): T

    override fun key(): Key = key.location

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun <T, V : T> of(registry: Registry<T>, key: Key): RegistryReference<V>
    }

    public companion object {

        /*
         * Notes for those using this within the API:
         *
         * It is up to the caller to ensure that the registry contains values
         * of type V, and also, that the value mapped to the key is of type V.
         *
         * This will NOT be verified by platforms, and WILL result in a
         * ClassCastException when `get` is called on the returned reference.
         */
        @JvmSynthetic
        internal fun <T, V : T> of(registry: Registry<T>, key: Key): RegistryReference<V> = Krypton.factory<Factory>().of(registry, key)
    }
}
