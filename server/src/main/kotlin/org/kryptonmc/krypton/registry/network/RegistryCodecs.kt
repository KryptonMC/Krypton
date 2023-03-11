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

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.KryptonSimpleRegistry
import org.kryptonmc.krypton.registry.holder.HolderSet
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

object RegistryCodecs {

    @JvmStatic
    private fun <T> withNameAndId(key: ResourceKey<out Registry<T>>, elementCodec: MapCodec<T>): MapCodec<RegistryEntry<T>> {
        return RecordCodecBuilder.createMap { instance ->
            instance.group(
                KryptonResourceKey.codec(key).fieldOf("name").getting { it.key },
                Codec.INT.fieldOf("id").getting { it.id },
                elementCodec.getting { it.value }
            ).apply(instance, ::RegistryEntry)
        }
    }

    @JvmStatic
    fun <T> network(key: ResourceKey<out Registry<T>>, elementCodec: Codec<T>): Codec<KryptonRegistry<T>> {
        return withNameAndId(key, elementCodec.fieldOf("element")).codec().listOf().xmap({ entries ->
            val registry = KryptonSimpleRegistry.standard(key)
            entries.forEach { entry -> registry.register(entry.id, entry.key, entry.value) }
            registry
        }, { registry ->
            val result = ImmutableList.builder<RegistryEntry<T>>()
            registry.forEach { value -> result.add(RegistryEntry(registry.getResourceKey(value)!!, registry.getId(value), value)) }
            result.build()
        })
    }

    @JvmStatic
    fun <E> full(key: ResourceKey<out Registry<E>>, elementCodec: Codec<E>): Codec<KryptonRegistry<E>> {
        val elementsCodec = Codec.map(KryptonResourceKey.codec(key), elementCodec)
        return elementsCodec.xmap({ elements ->
            val registry = KryptonSimpleRegistry.standard(key)
            elements.forEach { (key, value) -> registry.register(key, value) }
            registry.freeze()
        }, { ImmutableMap.copyOf(it.entries()) })
    }

    @JvmStatic
    fun <E> homogenousList(key: ResourceKey<out Registry<E>>, elementCodec: Codec<E>): Codec<HolderSet<E>> = homogenousList(key, elementCodec, false)

    @JvmStatic
    fun <E> homogenousList(key: ResourceKey<out Registry<E>>, elementCodec: Codec<E>, noInline: Boolean): Codec<HolderSet<E>> =
        HolderSetCodec.create(key, RegistryFileCodec.create(key, elementCodec), noInline)

    @JvmRecord
    private data class RegistryEntry<T>(val key: ResourceKey<T>, val id: Int, val value: T)
}
