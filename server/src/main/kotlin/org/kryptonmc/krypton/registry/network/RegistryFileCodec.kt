/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
