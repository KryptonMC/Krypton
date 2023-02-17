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
import org.kryptonmc.api.registry.DynamicRegistryReference
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKey

class KryptonDynamicRegistryReference<T>(
    private val registry: ResourceKey<out Registry<T>>,
    override val key: ResourceKey<T>
) : DynamicRegistryReference<T> {

    override fun get(holder: RegistryHolder): T {
        return requireNotNull(holder.getRegistry(registry)?.get(key)) { "Could not find value for key $key in holder $holder!" }
    }

    object Factory : DynamicRegistryReference.Factory {

        override fun <T> of(registry: ResourceKey<out Registry<T>>, key: Key): DynamicRegistryReference<T> {
            return KryptonDynamicRegistryReference(registry, KryptonResourceKey.of(registry, key))
        }
    }
}
