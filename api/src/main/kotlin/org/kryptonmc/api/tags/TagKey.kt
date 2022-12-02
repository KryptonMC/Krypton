/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
