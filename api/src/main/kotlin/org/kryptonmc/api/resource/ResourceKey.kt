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
package org.kryptonmc.api.resource

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A key pointing to some form of resource.
 *
 * @param T the type of this key
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(ResourceKeys::class)
@ImmutableType
public interface ResourceKey<T> {

    /**
     * The key of the parent registry.
     */
    @get:JvmName("registry")
    public val registry: Key

    /**
     * The location of the registry.
     */
    @get:JvmName("location")
    public val location: Key

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun <T> of(registry: Key, location: Key): ResourceKey<T>
    }

    public companion object {

        /**
         * Creates a new resource key, or returns an existing one if one with
         * the given parameters has already been created, with the given
         * [registry] as its parent name, and the given [location] as the
         * location of the resource.
         *
         * @param T the resource key type
         * @param registry the parent registry name
         * @param location the location of the resource
         * @return a resource key
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun <T> of(registry: Key, location: Key): ResourceKey<T> = Krypton.factory<Factory>().of(registry, location)

        /**
         * Creates a new resource key, or returns an existing one if one with
         * the given parameters has already been created, with the given
         * [parent] as its parent, and the given [location] as the location of
         * the resource.
         *
         * @param T the resource key type
         * @param parent the parent key
         * @param location the location of the resource
         * @return a resource key
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun <T> of(parent: ResourceKey<out Registry<T>>, location: Key): ResourceKey<T> = of(parent.location, location)
    }
}
