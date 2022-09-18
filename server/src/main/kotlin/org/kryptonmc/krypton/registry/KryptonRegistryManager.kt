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
package org.kryptonmc.krypton.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.DefaultedRegistry
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys

@Suppress("UNCHECKED_CAST")
object KryptonRegistryManager : RegistryManager {

    private lateinit var parent: KryptonRegistry<Registry<out Any>>

    @JvmStatic
    fun bootstrap() {
        parent = KryptonRegistry(ResourceKeys.PARENT)
    }

    override fun <T : Any> registry(key: ResourceKey<out Registry<T>>): Registry<T>? = parent[key as ResourceKey<Registry<out Any>>] as? Registry<T>

    override fun <T : Any> defaulted(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>? =
        parent[key as ResourceKey<Registry<out Any>>] as? DefaultedRegistry<T>

    override fun <T : Any> create(key: ResourceKey<out Registry<T>>): KryptonRegistry<T> =
        parent.register(key as ResourceKey<Registry<out Any>>, KryptonRegistry(key as ResourceKey<out Registry<T>>))

    override fun <T : Any> createDefaulted(key: ResourceKey<out Registry<T>>, defaultKey: Key): KryptonDefaultedRegistry<T> =
        parent.register(key as ResourceKey<Registry<out Any>>, KryptonDefaultedRegistry(key as ResourceKey<out Registry<T>>, defaultKey))
}
