/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import java.util.UUID

/**
 * A simple cache that holds [GameProfile]s.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ProfileCache : Iterable<GameProfile> {

    /**
     * An immutable view of the currently cached profiles.
     */
    @get:JvmName("profiles")
    public val profiles: Set<GameProfile>

    /**
     * Gets the cached [GameProfile] with the specified [name], or returns null
     * if there is no cached [GameProfile] with the specified name.
     *
     * @param name the name
     * @return the cached profile, or null if not present
     */
    public fun get(name: String): GameProfile?

    /**
     * Gets the cached [GameProfile] with the specified [uuid], or returns null
     * if there is no cached [GameProfile] with the specified [uuid].
     *
     * @param uuid the uuid
     * @return the cached profile, or null if not present
     */
    public fun get(uuid: UUID): GameProfile?
}
