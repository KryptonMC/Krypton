/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.plugins.bans.api

import org.kryptonmc.api.auth.GameProfile

/**
 * The manager of the vanilla ban system.
 */
interface BanManager {

    /**
     * Checks if the given [profile] has a registered ban associated with it.
     *
     * @return true if the profile is banned
     */
    fun isBanned(profile: GameProfile): Boolean

    /**
     * Checks if the given [ip] as a registered ban associated with it.
     *
     * @return true if the IP is banned
     */
    fun isBanned(ip: String): Boolean

    /**
     * Gets the registered ban for the given [profile], if one exists.
     * If the profile is not banned, this will return null.
     *
     * @param profile the profile to get the ban for
     * @return the ban, or null if the profile is not banned
     */
    fun getBan(profile: GameProfile): ProfileBan?

    /**
     * Gets the registered ban for the given [ip], if one exists.
     * If the IP is not banned, this will return null.
     *
     * @param ip the IP to get the ban for
     * @return the ban, or null if the IP is not banned
     */
    fun getBan(ip: String): IpBan?

    /**
     * Bans the profile in the given [ban] from the server, kicking the
     * associated player if currently online.
     *
     * The returned future indicates whether the ban was successful or not.
     * If the ban was not successful, it is likely that another plugin denied
     * the ban in an event.
     *
     * @param ban the ban
     * @return the result of the ban
     */
    fun banProfile(ban: ProfileBan): Boolean

    /**
     * Bans the IP in the given [ban] from the server, kicking any associated
     * players currently online.
     *
     * The returned future indicates whether the ban was successful or not.
     * If the ban was not successful, it is likely that another plugin denied
     * the ban in an event.
     *
     * @param ban the ban
     * @return the result of the ban
     */
    fun banIp(ban: IpBan): Boolean

    /**
     * Pardons the given [profile] if it is banned.
     *
     * The returned future indicates whether the pardon was successful or not.
     * If the pardon was not successful, it is likely that another plugin
     * denied the pardon in an event.
     *
     * @param profile the profile to pardon
     * @return the result of the pardon
     */
    fun pardonProfile(profile: GameProfile): Boolean

    /**
     * Pardons the given [ip] if it is banned.
     *
     * The returned future indicates whether the pardon was successful or not.
     * If the pardon was not successful, it is likely that another plugin
     * denied the pardon in an event.
     *
     * @param ip the IP to pardon
     * @return the result of the pardon
     */
    fun pardonIp(ip: String): Boolean

    /**
     * Gets all the registered profile bans.
     *
     * @return all the profile bans
     */
    fun allProfileBans(): Collection<ProfileBan>

    /**
     * Gets all the registered IP bans.
     *
     * @return all the IP bans
     */
    fun allIpBans(): Collection<IpBan>
}
