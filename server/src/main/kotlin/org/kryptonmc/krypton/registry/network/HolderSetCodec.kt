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
import org.kryptonmc.krypton.registry.holder.HolderSet
import org.kryptonmc.krypton.tags.KryptonTagKey
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.util.Either
import org.kryptonmc.util.Pair
import java.util.function.Function

class HolderSetCodec<E> private constructor(
    private val registryKey: ResourceKey<out Registry<E>>,
    private val elementCodec: Codec<Holder<E>>,
    noInline: Boolean
) : Codec<HolderSet<E>> {

    private val homogenousListCodec = homogenousList(elementCodec, noInline)
    private val registryAwareCodec = Codec.either(KryptonTagKey.hashedCodec(registryKey), homogenousListCodec)

    override fun <T> decode(input: T, ops: DataOps<T>): DataResult<Pair<HolderSet<E>, T>> {
        if (ops is RegistryOps<T>) {
            val getter = ops.getter(registryKey)
            if (getter != null) {
                return registryAwareCodec.decode(input, ops)
                    .map { pair -> pair.mapFirst { either -> either.map({ getter.getOrThrow(it) }, { HolderSet.direct(it) }) } }
            }
        }
        return decodeWithoutRegistry(input, ops)
    }

    override fun <T> encode(input: HolderSet<E>, ops: DataOps<T>, prefix: T & Any): DataResult<T> {
        if (ops is RegistryOps<T>) {
            val owner = ops.owner(registryKey)
            if (owner != null) {
                if (!input.canSerializeIn(owner)) return DataResult.error("HolderSet $input is not valid in current registry set!")
                return registryAwareCodec.encode(input.unwrap().mapRight { ImmutableLists.copyOf(it) }, ops, prefix)
            }
        }
        return encodeWithoutRegistry(input, ops, prefix)
    }

    private fun <T> decodeWithoutRegistry(value: T, ops: DataOps<T>): DataResult<Pair<HolderSet<E>, T>> {
        return elementCodec.listOf().decode(value, ops).flatMap { input ->
            val holders = ArrayList<Holder.Direct<E>>()
            input.first.forEach {
                if (it !is Holder.Direct) return@flatMap DataResult.error("Cannot decode element $it without registry!")
                holders.add(it)
            }
            DataResult.success(Pair.of(HolderSet.direct(holders), input.second))
        }
    }

    private fun <T> encodeWithoutRegistry(input: HolderSet<E>, ops: DataOps<T>, prefix: T & Any): DataResult<T> =
        homogenousListCodec.encode(input.stream().toList(), ops, prefix)

    companion object {

        @JvmStatic
        fun <E> create(key: ResourceKey<out Registry<E>>, elementCodec: Codec<Holder<E>>, noInline: Boolean): Codec<HolderSet<E>> =
            HolderSetCodec(key, elementCodec, noInline)

        @JvmStatic
        private fun <E> homogenousList(elementCodec: Codec<Holder<E>>, noInline: Boolean): Codec<List<Holder<E>>> {
            val homogenousChecker: Function<List<Holder<E>>, DataResult<List<Holder<E>>>> = Codecs.ensureHomogenous { it.kind() }
            val homogenousCodec = elementCodec.listOf().flatXmap(homogenousChecker, homogenousChecker)
            if (noInline) return homogenousCodec
            return Codec.either(homogenousCodec, elementCodec)
                .xmap({ either -> either.map({ it }, ImmutableLists::of) }, { if (it.size == 1) Either.right(it.get(0)) else Either.left(it) })
        }
    }
}
