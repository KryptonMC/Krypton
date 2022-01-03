/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user

import net.kyori.adventure.identity.Identified
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.entity.player.Player
import java.time.Instant
import java.util.UUID

/**
 * A user. This is a player that can be offline, and mostly exists for that
 * purpose.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface User : Identified {

    /**
     * The name of this user.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The UUID of this user.
     */
    @get:JvmName("uuid")
    public val uuid: UUID

    /**
     * The cached game profile associated with this user.
     */
    @get:JvmName("profile")
    public val profile: GameProfile

    /**
     * If this user is online or not.
     */
    public val isOnline: Boolean

    /**
     * The player that this user is associated with, or null if they are not
     * currently available, usually meaning they are offline.
     */
    @get:JvmName("player")
    public val player: Player?

    /**
     * If this user has joined this server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The time that this user first joined the server.
     */
    @get:JvmName("firstJoined")
    public val firstJoined: Instant

    /**
     * The latest time when this user last joined the server.
     */
    @get:JvmName("lastJoined")
    public val lastJoined: Instant

    /**
     * If this user is vanished, meaning they cannot be seen by other players.
     */
    public val isVanished: Boolean
}
