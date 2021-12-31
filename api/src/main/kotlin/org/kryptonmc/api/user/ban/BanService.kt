/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user.ban

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile
import java.net.InetAddress
import java.time.OffsetDateTime

/**
 * A service that provides a way to ban users.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BanService {

    /**
     * All of the registered profile bans.
     */
    @get:JvmName("profileBans")
    public val profileBans: Collection<Ban.Profile>

    /**
     * All of the registered IP bans.
     */
    @get:JvmName("ipBans")
    public val ipBans: Collection<Ban.IP>

    /**
     * Checks if the given [ban] is registered with this ban manager.
     *
     * @param ban the ban to check
     * @return true if the ban is registered, false otherwise
     */
    public operator fun contains(ban: Ban): Boolean

    /**
     * Gets the profile ban targeting the given [profile], or returns null if
     * there is no profile ban targeting the given [profile].
     *
     * @param profile the profile
     * @return the profile ban targeting the profile, or null if not present
     */
    public operator fun get(profile: GameProfile): Ban.Profile?

    /**
     * Gets the IP ban targeting the given [address], or returns null if there
     * is no IP ban targeting the given [address].
     *
     * @param address the address
     * @return the IP ban targeting the address, or null if not present
     */
    public operator fun get(address: InetAddress): Ban.IP?

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

    /**
     * Creates a new builder for building a ban with the given [type].
     *
     * @param type the type of the ban
     * @return a new ban builder
     */
    @Contract("_ -> new", pure = true)
    public fun createBuilder(type: BanType): Ban.Builder

    /**
     * Creates a new profile ban for the given [profile], expiring on the given
     * [expirationDate].
     *
     * @param profile the profile to ban
     * @param source the source of the ban
     * @param reason the reason why the profile was banned
     * @param expirationDate the date when the ban expires
     * @return a new profile ban for the profile
     */
    @Contract("_ -> new", pure = true)
    public fun createProfileBan(
        profile: GameProfile,
        source: Component,
        reason: Component?,
        expirationDate: OffsetDateTime?
    ): Ban = createBuilder(BanTypes.PROFILE)
        .profile(profile)
        .apply { if (expirationDate != null) expirationDate(expirationDate) }
        .build()

    /**
     * Creates a new IP ban for the given IP [address], expiring on the given
     * [expirationDate].
     *
     * @param address the IP address to ban
     * @param source the source of the ban
     * @param reason the reason why the IP was banned
     * @param expirationDate the date when the ban expires
     * @return a new IP ban for the IP address
     */
    @Contract("_ -> new", pure = true)
    public fun createIPBan(
        address: InetAddress,
        source: Component,
        reason: Component?,
        expirationDate: OffsetDateTime?
    ): Ban = createBuilder(BanTypes.PROFILE)
        .address(address)
        .apply { if (expirationDate != null) expirationDate(expirationDate) }
        .build()
}
