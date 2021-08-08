/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import java.util.UUID

/**
 * A simple cache of [GameProfile]s.
 */
interface ProfileCache : Iterable<GameProfile> {

    /**
     * All the currently cached profiles.
     */
    val profiles: Set<GameProfile>

    /**
     * Gets the cached [GameProfile] with the specified [name], or returns null
     * if there is no cached [GameProfile] with the specified name.
     *
     * Warning: This **will** perform a **blocking** request to retrieve the
     * profile if it is not cached!
     *
     * @param name the name
     * @return the cached profile, or null if not present
     */
    operator fun get(name: String): GameProfile?

    /**
     * Gets the cached [GameProfile] with the specified [uuid], or returns null
     * if there is no cached [GameProfile] with the specified [uuid].
     *
     * @param uuid the uuid
     * @return the cached profile, or null if not present
     */
    operator fun get(uuid: UUID): GameProfile?
}
