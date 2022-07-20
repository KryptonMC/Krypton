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
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
@JvmRecord
data class AdvancedCategory(
    @Setting("synchronize-chunk-writes")
    @Comment("Whether to use the DSYNC option for saving region files to disk.")
    val synchronizeChunkWrites: Boolean = true,
    @Setting("serialize-player-data")
    @Comment("Whether the server should load and save player data to and from files")
    val serializePlayerData: Boolean = true,
    @Setting("use-data-converter")
    @Comment("If worlds from previous versions should be upgraded to the current version.")
    val useDataConverter: Boolean = true,
    @Comment("If the server should enforce that all game profiles have properties with valid signatures.")
    @Setting("enforce-secure-profiles")
    val enforceSecureProfiles: Boolean = true
)
