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
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import java.util.UUID
import java.util.function.Consumer

/**
 * Represents the profile of an authenticated player. Most of the time, this
 * authentication will be done with Mojang, but it may not, depending on the
 * implementation.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface GameProfile : Buildable<GameProfile, GameProfile.Builder>, Identified {

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
    public val properties: List<ProfileProperty>

    /**
     * Creates a new game profile by creating a new builder from this profile
     * and applying the given [builder] to it.
     *
     * @param builder the builder to apply
     * @return a new game profile
     */
    @JvmSynthetic
    @Contract("_ -> new", pure = true)
    public fun with(builder: Builder.() -> Unit): GameProfile

    /**
     * Creates a new game profile by creating a new builder from this profile
     * and applying the given [builder] to it.
     *
     * @param builder the builder to apply
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun with(builder: Consumer<Builder>): GameProfile = with { builder.accept(this) }

    /**
     * Creates a new game profile with the given [name].
     *
     * @param name the new name
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withName(name: String): GameProfile

    /**
     * Creates a new game profile with the given [uuid].
     *
     * @param uuid the new UUID
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withUUID(uuid: UUID): GameProfile

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

    /**
     * A builder for building game profiles.
     *
     * This is designed for bulk modification, and so cannot be constructed
     * without an existing instance.
     */
    public interface Builder : Buildable.Builder<GameProfile> {

        /**
         * Sets the name of the game profile to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): Builder

        /**
         * Sets the UUID of the game profile to the given [uuid].
         *
         * @param uuid the UUID
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun uuid(uuid: UUID): Builder

        /**
         * Sets the properties of the game profile to the given [properties].
         *
         * @param properties the properties
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun properties(properties: Iterable<ProfileProperty>): Builder

        /**
         * Adds the given [property] to the list of properties for the game
         * profile.
         *
         * @param property the property to add
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addProperty(property: ProfileProperty): Builder

        /**
         * Removes the property at the given [index] from the list of
         * properties for the game profile.
         *
         * @param index the index of the property to remove
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun removeProperty(index: Int): Builder

        /**
         * Removes the given [property] from the list of properties for the
         * game profile.
         *
         * @param property the property to remove
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun removeProperty(property: ProfileProperty): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, uuid: UUID, properties: List<ProfileProperty> = emptyList()): GameProfile
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new game profile with the given [name] and [uuid].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @return a new profile
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(name: String, uuid: UUID): GameProfile = FACTORY.of(name, uuid)

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
        @Contract("_ -> new", pure = true)
        public fun of(
            name: String,
            uuid: UUID,
            properties: List<ProfileProperty>
        ): GameProfile = FACTORY.of(name, uuid, properties)
    }
}
