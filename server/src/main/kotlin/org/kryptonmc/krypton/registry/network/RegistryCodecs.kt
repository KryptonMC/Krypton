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
