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

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys

object InternalRegistries {

    val SOUND_EVENT = create(ResourceKeys.SOUND_EVENT)
    val ENTITY_TYPE = createDefaulted(ResourceKeys.ENTITY_TYPE, key("pig"))
    val PARTICLE_TYPE = create(ResourceKeys.PARTICLE_TYPE)
    val BLOCK = create(ResourceKeys.BLOCK)
    val ITEM = createDefaulted(ResourceKeys.ITEM, key("air"))
    val MENU = create(ResourceKeys.MENU)
    val ATTRIBUTE = create(ResourceKeys.ATTRIBUTE)
    val MEMORIES = create(InternalResourceKeys.MEMORIES)
    val GAME_EVENT = create(InternalResourceKeys.GAME_EVENT)
    val FLUID = createDefaulted(InternalResourceKeys.FLUID, key("empty"))
    val BIOME = create(InternalResourceKeys.BIOME)
    val STATISTIC_TYPE = create(ResourceKeys.STATISTIC_TYPE)
    val CANVAS = createDefaulted(ResourceKeys.CANVAS, key("kebab"))

    // World generation registries
    val GENERATOR = create(InternalResourceKeys.GENERATOR)
    val BIOME_GENERATOR = create(InternalResourceKeys.BIOME_GENERATOR)
    val FEATURE = create(InternalResourceKeys.FEATURE)
    val STRUCTURE = create(InternalResourceKeys.STRUCTURE)
    val NOISE_GENERATOR_SETTINGS = create(InternalResourceKeys.NOISE_GENERATOR_SETTINGS)
    val CHUNK_STATUS = createDefaulted(InternalResourceKeys.CHUNK_STATUS, key("empty"))

    private fun <T : Any> create(key: ResourceKey<out Registry<T>>) = KryptonRegistryManager.create(key)

    private fun <T : Any> createDefaulted(key: ResourceKey<out Registry<T>>, defaultKey: Key) = KryptonRegistryManager.createDefaulted(key, defaultKey)
}
