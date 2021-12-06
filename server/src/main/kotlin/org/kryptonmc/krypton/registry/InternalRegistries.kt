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
import org.kryptonmc.krypton.resource.InternalResourceKeys

object InternalRegistries {

    @JvmField val SOUND_EVENT = create(ResourceKeys.SOUND_EVENT)
    @JvmField val ENTITY_TYPE = createDefaulted(ResourceKeys.ENTITY_TYPE, key("pig"))
    @JvmField val PARTICLE_TYPE = create(ResourceKeys.PARTICLE_TYPE)
    @JvmField val BLOCK = create(ResourceKeys.BLOCK)
    @JvmField val ITEM = createDefaulted(ResourceKeys.ITEM, key("air"))
    @JvmField val MENU = create(ResourceKeys.MENU)
    @JvmField val ATTRIBUTE = create(ResourceKeys.ATTRIBUTE)
    @JvmField val MEMORIES = create(InternalResourceKeys.MEMORIES)
    @JvmField val GAME_EVENT = create(InternalResourceKeys.GAME_EVENT)
    @JvmField val BIOME = create(ResourceKeys.BIOME)
    @JvmField val STATISTIC_TYPE = create(ResourceKeys.STATISTIC_TYPE)
    @JvmField val PICTURE = createDefaulted(ResourceKeys.PICTURE, key("kebab"))
    @JvmField val FLUID = createDefaulted(ResourceKeys.FLUID, key("empty"))
    @JvmField val BLOCK_ENTITY_TYPE = create(ResourceKeys.BLOCK_ENTITY_TYPE)

    // World generation registries
    @JvmField val GENERATOR = create(InternalResourceKeys.GENERATOR)
    @JvmField val BIOME_GENERATOR = create(InternalResourceKeys.BIOME_GENERATOR)
    @JvmField val STRUCTURE = create(InternalResourceKeys.STRUCTURE)
    @JvmField val NOISE_GENERATOR_SETTINGS = create(InternalResourceKeys.NOISE_GENERATOR_SETTINGS)
    @JvmField val CHUNK_STATUS = createDefaulted(InternalResourceKeys.CHUNK_STATUS, key("empty"))

    @JvmStatic
    private fun <T : Any> create(key: ResourceKey<out Registry<T>>): KryptonRegistry<T> = KryptonRegistryManager.create(key)

    @JvmStatic
    private fun <T : Any> createDefaulted(
        key: ResourceKey<out Registry<T>>,
        defaultKey: Key
    ): KryptonDefaultedRegistry<T> = KryptonRegistryManager.createDefaulted(key, defaultKey)
}
