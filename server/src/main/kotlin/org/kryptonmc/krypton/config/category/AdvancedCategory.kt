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
package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
@JvmRecord
data class AdvancedCategory(
    @Comment("Whether to use the DSYNC option for saving region files to disk.")
    val synchronizeChunkWrites: Boolean = true,
    @Comment("Whether the server should load and save player data to and from files")
    val serializePlayerData: Boolean = true,
    @Comment("If the server should enforce that all game profiles have properties with valid signatures.")
    val enforceSecureProfiles: Boolean = true,
    @Comment("The maximum number of updates to block neighbours that may be executed in bulk.")
    val maximumChainedNeighbourUpdates: Int = 1000000,
    @Comment("If we should enable bStats metrics for the server")
    val metrics: Boolean = true,
    @Comment("The duration (in seconds) a single tick must take before the single tick profiler reports it.")
    val saveThreshold: Int = 5,
    @Comment("Whether player chat messages should be sent to the console. Disable if you have a lot of players sending a lot of messages.")
    val logPlayerChatMessages: Boolean = true
)
