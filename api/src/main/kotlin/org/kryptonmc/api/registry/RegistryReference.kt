/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
