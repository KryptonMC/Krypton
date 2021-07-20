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
package org.kryptonmc.krypton.registry.ops

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.util.DelegatingOps
import org.kryptonmc.krypton.util.KEY_CODEC

class RegistryWriteOps<T : Any>(
    delegate: DynamicOps<T>,
    private val registryHolder: RegistryHolder
) : DelegatingOps<T>(delegate) {

    fun <E : Any> encode(input: E, prefix: T, key: ResourceKey<out Registry<E>>, elementCodec: Codec<E>): DataResult<T> {
        val registry = registryHolder.ownedRegistry(key) ?: return elementCodec.encode(input, this, prefix)
        val ownedKey = registry.resourceKey(input) ?: return elementCodec.encode(input, this, prefix)
        return KEY_CODEC.encode(ownedKey.location, delegate, prefix)
    }
}
