/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.resource

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.util.CataloguedBy
import javax.annotation.concurrent.Immutable

/**
 * A key pointing to some form of resource.
 *
 * @param T the type of this key
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(ResourceKeys::class)
@Immutable
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
