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
