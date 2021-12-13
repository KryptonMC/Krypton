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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.resource.InternalResourceKeys

object InternalRegistries {

    @JvmField val MEMORIES = create(InternalResourceKeys.MEMORIES)
    @JvmField val GAME_EVENT = create(InternalResourceKeys.GAME_EVENT)

    // World generation registries
    @JvmField val STRUCTURE = create(InternalResourceKeys.STRUCTURE)
    @JvmField val NOISE_GENERATOR_SETTINGS = create(InternalResourceKeys.NOISE_GENERATOR_SETTINGS)
    @JvmField val CHUNK_STATUS = createDefaulted(InternalResourceKeys.CHUNK_STATUS, Key.key("empty"))

    @JvmStatic
    private fun <T : Any> create(key: ResourceKey<out Registry<T>>): KryptonRegistry<T> = KryptonRegistryManager.create(key)

    @JvmStatic
    private fun <T : Any> createDefaulted(
        key: ResourceKey<out Registry<T>>,
        defaultKey: Key
    ): KryptonDefaultedRegistry<T> = KryptonRegistryManager.createDefaulted(key, defaultKey)
}
