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
package org.kryptonmc.krypton.registry.network

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.Lifecycle
import org.kryptonmc.util.Pair

class RegistryFixedCodec<E> private constructor(private val registryKey: ResourceKey<out Registry<E>>) : Codec<Holder<E>> {

    override fun <T> encode(input: Holder<E>, ops: DataOps<T>, prefix: T & Any): DataResult<T> {
        if (ops is RegistryOps<T>) {
            val owner = ops.owner(registryKey)
            if (owner != null) {
                if (!input.canSerializeIn(owner)) return DataResult.error("Element $input is not valid in current registry set!")
                return input.unwrap().map(
                    { Codecs.KEY.encode(it.location, ops, prefix) },
                    { DataResult.error("Elements from registry $registryKey cannot be serialized to a value") }
                )
            }
        }
        return DataResult.error("Cannot access registry $registryKey!")
    }

    override fun <T> decode(input: T, ops: DataOps<T>): DataResult<Pair<Holder<E>, T>> {
        if (ops is RegistryOps<T>) {
            val getter = ops.getter(registryKey)
            if (getter != null) {
                return Codecs.KEY.decode(input, ops).flatMap { pair ->
                    @Suppress("UNCHECKED_CAST")
                    getter.get(KryptonResourceKey.of(registryKey, pair.first))
                        .resultOrError { "Failed to get element ${pair.first}!" }
                        .map { Pair.of(it, pair.second as T) }
                        .withLifecycle(Lifecycle.stable()) as DataResult<Pair<Holder<E>, T>>
                }
            }
        }
        return DataResult.error("Cannot access registry $registryKey!")
    }

    override fun toString(): String = "RegistryFixedCodec[$registryKey]"
}
