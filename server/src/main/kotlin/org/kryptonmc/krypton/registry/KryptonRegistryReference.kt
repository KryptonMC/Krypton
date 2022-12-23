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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKey

class KryptonRegistryReference<T, V : T>(private val registry: Registry<T>, override val key: ResourceKey<V>) : RegistryReference<V> {

    @Suppress("UNCHECKED_CAST")
    override fun get(): V = registry.get(key as ResourceKey<T>) as V

    object Factory : RegistryReference.Factory {

        override fun <T, V : T> of(registry: Registry<T>, key: Key): RegistryReference<V> =
            KryptonRegistryReference(registry, KryptonResourceKey.of(registry.key.location, key))
    }
}
