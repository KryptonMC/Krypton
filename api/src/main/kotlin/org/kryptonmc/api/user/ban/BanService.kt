/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user.ban

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile
import java.net.InetAddress

/**
 * A service that provides a way to ban users.
 */
public interface BanService {

    /**
     * All of the registered profile bans.
     */
    public val profileBans: Collection<Ban.Profile>

    /**
     * All of the registered IP bans.
     */
    public val ipBans: Collection<Ban.IP>

    /**
     * Creates a new builder for building a ban.
     *
     * @return a new ban builder
     */
    @Contract("-> new", pure = true)
    public fun createBuilder(): Ban.Builder

    /**
     * Checks if the given [ban] is registered with this ban manager.
     *
     * @param ban the ban to check
     * @return true if the ban is registered, false otherwise
     */
    public fun isRegistered(ban: Ban): Boolean

    /**
     * Gets the profile ban targeting the given [profile], or returns null if
     * there is no profile ban targeting the given [profile].
     *
     * @param profile the profile
     * @return the profile ban targeting the profile, or null if not present
     */
    public fun get(profile: GameProfile): Ban.Profile?

    /**
     * Gets the IP ban targeting the given [address], or returns null if there
     * is no IP ban targeting the given [address].
     *
     * @param address the address
     * @return the IP ban targeting the address, or null if not present
     */
    public fun get(address: InetAddress): Ban.IP?

    /**
     * Attempts to pardon the given [profile], removing any bans associated
     * with them.
     *
     * @param profile the profile
     */
    public fun pardon(profile: GameProfile)

    /**
     * Attempts to pardon the given [address], removing any bans associated
     * with it.
     *
     * @param address the address
     */
    public fun pardon(address: InetAddress)

    /**
     * Adds the given [ban] to this ban manager.
     *
     * If the target of the given [ban] already has a ban registered, the
     * given [ban] will replace the existing ban.
     *
     * @param ban the ban to add
     */
    public fun add(ban: Ban)

    /**
     * Removes the given [ban] from this ban manager.
     *
     * @param ban the ban
     */
    public fun remove(ban: Ban)
}
