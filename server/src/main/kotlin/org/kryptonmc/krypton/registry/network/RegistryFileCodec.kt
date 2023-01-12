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
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.Lifecycle
import org.kryptonmc.util.Pair

class RegistryFileCodec<E> private constructor(
    private val registryKey: ResourceKey<out Registry<E>>,
    private val elementCodec: Codec<E>,
    private val allowInline: Boolean
) : Codec<Holder<E>> {

    override fun <T> encode(input: Holder<E>, ops: DataOps<T>, prefix: T & Any): DataResult<T> {
        if (ops is RegistryOps<T>) {
            val owner = ops.owner(registryKey)
            if (owner != null) {
                if (!input.canSerializeIn(owner)) return DataResult.error("Element $input is not valid in current registry set!")
                return input.unwrap().map({ Keys.CODEC.encode(it.location, ops, prefix) }, { elementCodec.encode(it, ops, prefix) })
            }
        }
        return elementCodec.encode(input.value(), ops, prefix)
    }

    override fun <T> decode(input: T, ops: DataOps<T>): DataResult<Pair<Holder<E>, T>> {
        if (ops is RegistryOps<T>) {
            val getter = ops.getter(registryKey) ?: return DataResult.error("Registry does not exist: $registryKey")
            val keyDecode = Keys.CODEC.decode(input, ops)
            if (keyDecode.result().isEmpty) {
                if (!allowInline) return DataResult.error("Inline definitions not allowed here!")
                return elementCodec.decode(input, ops).map { pair -> pair.mapFirst { Holder.Direct(it!!) } }
            }
            val keyResult = keyDecode.result().get()
            val key = KryptonResourceKey.of(registryKey, keyResult.first)
            @Suppress("UNCHECKED_CAST")
            return getter.get(key).resultOrError { "Failed to get element $key!" }
                .map { Pair.of(it, keyResult.second) }
                .withLifecycle(Lifecycle.stable()) as DataResult<Pair<Holder<E>, T>>
        }
        return elementCodec.decode(input, ops).map { pair -> pair.mapFirst { Holder.Direct(it!!) } }
    }

    override fun toString(): String = "RegistryFileCodec[$registryKey $elementCodec]"

    companion object {

        @JvmStatic
        fun <E> create(key: ResourceKey<out Registry<E>>, elementCodec: Codec<E>): RegistryFileCodec<E> = create(key, elementCodec, true)

        @JvmStatic
        fun <E> create(key: ResourceKey<out Registry<E>>, elementCodec: Codec<E>, allowInline: Boolean): RegistryFileCodec<E> =
            RegistryFileCodec(key, elementCodec, allowInline)
    }
}
