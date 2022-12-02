/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import net.kyori.adventure.identity.Identified
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.UUID

/**
 * The profile of an authenticated player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface GameProfile : Identified {

    /**
     * The name of the profile.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The UUID of the profile.
     */
    @get:JvmName("uuid")
    public val uuid: UUID

    /**
     * All of the properties for this profile.
     */
    @get:JvmName("properties")
    public val properties: @Unmodifiable List<ProfileProperty>

    /**
     * Creates a new game profile with the given [properties].
     *
     * @param properties the new properties
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withProperties(properties: Iterable<ProfileProperty>): GameProfile

    /**
     * Creates a new game profile with the given [property] added to the list
     * of properties.
     *
     * @param property the property to add
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withProperty(property: ProfileProperty): GameProfile

    /**
     * Creates a new game profile with the property at the given [index]
     * removed from the list of properties.
     *
     * @param index the index of the property to remove
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withoutProperty(index: Int): GameProfile

    /**
     * Creates a new game profile with the given [property] removed from the
     * list of properties.
     *
     * @param property the property to remove
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withoutProperty(property: ProfileProperty): GameProfile

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile
    }

    public companion object {

        /**
         * Creates a new game profile with the given [name], [uuid], and list
         * of profile [properties].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @param properties the list of profile properties
         * @return a new profile
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile =
            Krypton.factory<Factory>().of(name, uuid, properties)

        /**
         * Creates a new game profile with the given [name] and [uuid].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @return a new profile
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(name: String, uuid: UUID): GameProfile = of(name, uuid, emptyList())
    }
}
