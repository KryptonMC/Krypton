/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton

import com.mojang.serialization.Dynamic
import org.kryptonmc.api.world.GameVersion
import org.kryptonmc.krypton.pack.repository.PackRepository
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.DataPackConfig

private val LOGGER = logger<KryptonServer>()

fun PackRepository.configure(config: DataPackConfig, safeMode: Boolean): DataPackConfig {
    reload()
    if (safeMode) {
        select(setOf("vanilla"))
        return DataPackConfig.DEFAULT
    }
    val packs = mutableListOf<String>()
    config.enabled.forEach { if (isAvailable(it)) packs.add(it) else LOGGER.warn("Missing data pack $it!") }
    availablePacks.forEach {
        val id = it.id
        if (!config.disabled.contains(id) && !packs.contains(id)) {
            LOGGER.info("Found new data pack $id, loading it automatically")
            packs.add(id)
        }
    }
    if (packs.isEmpty()) {
        LOGGER.info("No data packs selected, defaulting to loading built-in only")
        packs.add("vanilla")
    }
    select(packs)
    return selectedPacks()
}

private fun PackRepository.selectedPacks(): DataPackConfig {
    val ids = selectedIds
    val enabled = ids.toList()
    val disabled = availableIds.filter { !ids.contains(it) }
    return DataPackConfig(enabled, disabled)
}

fun Dynamic<*>.toGameVersion(): GameVersion = GameVersion(
    get("Id").asInt(ServerInfo.GAME_VERSION.id),
    get("Name").asString(ServerInfo.GAME_VERSION.name),
    get("Snapshot").asBoolean(ServerInfo.GAME_VERSION.isSnapshot)
)
