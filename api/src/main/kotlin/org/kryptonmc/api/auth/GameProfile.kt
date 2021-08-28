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
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide
import java.util.UUID

/**
 * Represents a player's authenticated Mojang game profile.
 */
interface GameProfile {

    /**
     * The name of the profile.
     */
    val name: String

    /**
     * The UUID of the profile.
     */
    val uuid: UUID

    /**
     * The list of properties for this profile.
     */
    val properties: List<ProfileProperty>

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    interface Factory {

        fun of(name: String, uuid: UUID, properties: List<ProfileProperty> = emptyList()): GameProfile
    }

    companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new [GameProfile] with the given [name] and [uuid].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @return a new profile with the given name and uuid
         */
        fun of(name: String, uuid: UUID): GameProfile = FACTORY.of(name, uuid)

        /**
         * Creates a new [GameProfile] with the given [name], [uuid], and list of
         * profile [properties].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @param properties the list of profile properties
         * @return a new profile with the given name, uuid, and list of profile properties
         */
        fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile = FACTORY.of(name, uuid, properties)
    }
}
