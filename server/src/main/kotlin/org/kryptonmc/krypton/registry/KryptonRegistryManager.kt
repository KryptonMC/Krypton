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
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.resource.ResourceKeys

object KryptonRegistryManager : RegistryManager {

    override val parent = KryptonRegistry(ResourceKeys.PARENT)

    override fun <T : Any> register(registry: Registry<T>, key: Key, value: T) = registry.register(ResourceKey.of(registry.key, key), value)

    override fun <T : Any> register(registry: Registry<T>, id: Int, key: Key, value: T) = registry.register(id, ResourceKey.of(registry.key, key), value)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(key: ResourceKey<out Registry<T>>) = parent.register(
        key as ResourceKey<Registry<out Any>>,
        KryptonRegistry(key as ResourceKey<out Registry<T>>)
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createDefaulted(key: ResourceKey<out Registry<T>>, defaultKey: Key) = parent.register(
        key as ResourceKey<Registry<out Any>>,
        KryptonDefaultedRegistry(key as ResourceKey<out Registry<T>>, defaultKey)
    )
}
