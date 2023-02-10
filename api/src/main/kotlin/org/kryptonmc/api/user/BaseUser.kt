/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user

import org.kryptonmc.api.auth.GameProfile
import java.time.Instant

/**
 * Common data associated with both users and players.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BaseUser {

    /**
     * The cached game profile associated with this user.
     */
    public val profile: GameProfile

    /**
     * If this user has joined this server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The time that this user first joined the server.
     */
    public val firstJoined: Instant

    /**
     * The latest time when this user last joined the server.
     */
    public val lastJoined: Instant

    /**
     * If this user is online or not.
     *
     * @return true if this user is online
     */
    public fun isOnline(): Boolean
}
