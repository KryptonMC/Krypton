/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.plugins.whitelist.api

import org.kryptonmc.api.auth.GameProfile
import java.util.concurrent.CompletableFuture

/**
 * The manager of the vanilla whitelist system.
 */
interface WhitelistManager {

    /**
     * Checks if the given [profile] is in the whitelist.
     *
     * @return true if the profile is whitelisted
     */
    fun isWhitelisted(profile: GameProfile): Boolean

    /**
     * Checks if the given [ip] is in the whitelist.
     *
     * @return true if the IP is whitelisted
     */
    fun isWhitelisted(ip: String): Boolean

    /**
     * Adds the given [profile] to the whitelist.
     *
     * The returned future indicates whether the profile was whitelisted
     * successfully or not.
     * If whitelisting the profile was not successful, it is likely that
     * another plugin denied the whitelist in an event.
     *
     * @param profile the profile
     * @return the result of the whitelist
     */
    fun whitelistProfile(profile: GameProfile): Boolean

    /**
     * Adds the given [ip] to the whitelist.
     *
     * The returned future indicates whether the IP was whitelisted
     * successfully or not.
     * If whitelisting the IP was not successful, it is likely that another
     * plugin denied the whitelist in an event.
     *
     * @param ip the IP
     * @return the result of the whitelist
     */
    fun whitelistIp(ip: String): Boolean

    /**
     * Removes the given [profile] from the whitelist.
     *
     * The returned future indicates whether the profile was successfully
     * removed from the whitelist or not.
     * If the profile was not successfully removed from the whitelist, it is
     * likely that another plugin denied the whitelist removal in an event.
     *
     * @param profile the profile to remove from the whitelist
     * @return the result of the whitelist removal
     */
    fun removeWhitelistedProfile(profile: GameProfile): Boolean

    /**
     * Removes the given [ip] address from the whitelist.
     *
     * The returned future indicates whether the IP was successfully removed
     * from the whitelist or not.
     * If the IP was not successfully removed from the whitelist, it is likely
     * that another plugin denied the whitelist removal in an event.
     *
     * @param ip the IP to remove from the whitelist
     * @return the result of the whitelist removal
     */
    fun removeWhitelistedIp(ip: String): Boolean

    /**
     * Gets all the registered profile bans.
     *
     * @return all the whitelisted profiles
     */
    fun allWhitelistedProfiles(): Collection<GameProfile>

    /**
     * Gets all the IP addresses that are whitelisted.
     *
     * @return all the whitelisted IP addresses
     */
    fun allWhitelistedIps(): Collection<String>
}
