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
package org.kryptonmc.krypton.config

import com.google.gson.JsonObject
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.registry.dynamic.RegistryAccess
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.ImmutableMaps
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.generation.WorldDimensions
import org.kryptonmc.krypton.world.generation.preset.WorldPresets
import java.util.Optional

@JvmRecord
data class WorldDimensionData(val generatorSettings: JsonObject, val worldType: String) {

    fun create(registryAccess: RegistryAccess): WorldDimensions {
        val registry = registryAccess.registryOrThrow(KryptonResourceKeys.WORLD_PRESET)
        val reference = Optional.ofNullable(registry.getHolder(WorldPresets.NORMAL))
            .or { registry.holders().findAny() }
            .orElseThrow { IllegalStateException("Invalid data pack contents: cannot find default preset") }
        val holder = Optional.ofNullable(Keys.create(worldType))
            .map { KryptonResourceKey.of(KryptonResourceKeys.WORLD_PRESET, it) }
            .or { Optional.ofNullable(LEGACY_PRESET_NAMES.get(worldType)) }
            .flatMap { Optional.ofNullable(registry.getHolder(it)) }
            .orElseGet {
                LOGGER.warn("Failed to parse world type $worldType. Defaulting to ${reference.key().location}")
                reference
            }
        return holder.value().createWorldDimensions()
    }

    companion object {

        private val LEGACY_PRESET_NAMES = ImmutableMaps.of("default", WorldPresets.NORMAL, "largebiomes", WorldPresets.LARGE_BIOMES)
        private val LOGGER = LogManager.getLogger()
    }
}
