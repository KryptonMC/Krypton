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
package org.kryptonmc.krypton.registry

import com.google.gson.reflect.TypeToken
import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.minecraftKey
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.registry.biomes.BiomeRegistry
import org.kryptonmc.krypton.registry.dimensions.DimensionRegistry
import org.kryptonmc.krypton.registry.json.RegistryEntry
import org.kryptonmc.krypton.util.fromJson

private val TYPE = object : TypeToken<Map<Key, RegistryEntry>>() {}.type
private val REGISTRIES: Map<Key, RegistryEntry> = GSON.fromJson(registryData("registries/registries.json"), TYPE)

/**
 * Singleton for all of the registries that we load from files. This is a singleton for ease of use
 *
 * @author Callum Seabrook
 */
object Registries {

    val BLOCKS = MappedRegistry(REGISTRIES.getValue(minecraftKey("block")).entries.mapValues { it.value.id })
    val FLUIDS = MappedRegistry(REGISTRIES.getValue(minecraftKey("fluid")).entries.mapValues { it.value.id })
    val ITEMS = MappedRegistry(REGISTRIES.getValue(minecraftKey("item")).entries.mapValues { it.value.id })
    val ENTITY_TYPES = MappedRegistry(REGISTRIES.getValue(minecraftKey("entity_type")).entries.mapValues { it.value.id })
    val SOUND_EVENTS = MappedRegistry(REGISTRIES.getValue(minecraftKey("sound_event")).entries.mapValues { it.value.id })

    val BIOMES = GSON.fromJson<BiomeRegistry>(registryData("registries/custom/biomes.json"))
    val DIMENSIONS = GSON.fromJson<DimensionRegistry>(registryData("registries/custom/dimensions.json"))
}

private fun registryData(path: String) = (Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    ?: throw IllegalStateException("$path not on classpath! Something has gone horribly wrong!"))
    .reader()
    .readText()
