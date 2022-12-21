/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user.whitelist

import org.kryptonmc.api.auth.GameProfile
import java.net.InetAddress

/**
 * A service that can whitelist users.
 */
public interface WhitelistService {

    /**
     * Gets whether the whitelist is currently enabled.
     *
     * @return true if enabled, false otherwise
     */
    public fun isEnabled(): Boolean

    /**
     * Checks if the given [profile] is whitelisted on the server.
     *
     * @param profile the profile to check
     * @return true if the profile is whitelisted, false otherwise
     */
    public fun isWhitelisted(profile: GameProfile): Boolean

    /**
     * Checks if the given IP [address] is whitelisted on the server.
     *
     * @param address the address to check
     * @return true if the address is whitelisted, false otherwise
     */
    public fun isWhitelisted(address: InetAddress): Boolean

    /**
     * Adds the given [profile] to the whitelist.
     *
     * @param profile the profile to add
     */
    public fun whitelist(profile: GameProfile)

    /**
     * Adds the given [address] to the whitelist.
     *
     * @param address the address to add
     */
    public fun whitelist(address: InetAddress)

    /**
     * Removes the given [profile] from the whitelist.
     *
     * @param profile the profile to remove
     */
    public fun removeWhitelisted(profile: GameProfile)

    /**
     * Removes the given [address] from the whitelist.
     *
     * @param address the address to remove
     */
    public fun removeWhitelisted(address: InetAddress)
}
