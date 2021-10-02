/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import java.util.UUID

/**
 * Represents the profile of an authenticated player. Most of the time, this
 * authentication will be done with Mojang, but it may not, depending on the
 * implementation.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface GameProfile {

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
     * The list of properties for this profile.
     */
    @get:JvmName("properties")
    public val properties: List<ProfileProperty>

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, uuid: UUID, properties: List<ProfileProperty> = emptyList()): GameProfile
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new [GameProfile] with the given [name] and [uuid].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @return a new profile with the given name and uuid
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(name: String, uuid: UUID): GameProfile = FACTORY.of(name, uuid)

        /**
         * Creates a new [GameProfile] with the given [name], [uuid], and list of
         * profile [properties].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @param properties the list of profile properties
         * @return a new profile with the given name, uuid, and list of profile
         * properties
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
