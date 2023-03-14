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
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A key for registry tags.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface TagKey<T> {

    /**
     * The key for the registry that this tag key is for.
     */
    @get:JvmName("registry")
    public val registry: ResourceKey<out Registry<T>>

    /**
     * The location of this tag key.
     */
    @get:JvmName("location")
    public val location: Key

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun <T> of(registry: ResourceKey<out Registry<T>>, location: Key): TagKey<T>
    }

    public companion object {

        /**
         * Creates a new tag key for the given [registry] and [location].
         *
         * @param T the tag type
         * @param registry the registry key
         * @param location the location
         * @return a new tag key
         */
        @JvmStatic
        public fun <T> of(registry: ResourceKey<out Registry<T>>, location: Key): TagKey<T> = Krypton.factory<Factory>().of(registry, location)
    }
}
