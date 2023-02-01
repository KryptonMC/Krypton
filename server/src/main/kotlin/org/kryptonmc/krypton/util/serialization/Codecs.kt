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
package org.kryptonmc.krypton.util.serialization

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.successOrError
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.MapLike
import org.kryptonmc.serialization.RecordBuilder
import org.kryptonmc.util.Pair
import java.util.Arrays
import java.util.BitSet
import java.util.Optional
import java.util.function.Function
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import java.util.stream.IntStream

object Codecs {

    @JvmField
    val PATTERN: Codec<Pattern> = Codec.STRING.comapFlatMap(
        {
            try {
                DataResult.success(Pattern.compile(it))
            } catch (exception: PatternSyntaxException) {
                DataResult.error("Invalid regex pattern $it: ${exception.message}")
            }
        },
        { it.pattern() }
    )
    // TODO: Look at the particle type codec, since it's not that great here
    @JvmField
    val PARTICLE: Codec<ParticleType> = Keys.CODEC.xmap(
        { KryptonRegistries.PARTICLE_TYPE.get(it)!! },
        { KryptonRegistries.PARTICLE_TYPE.getKey(it)!! }
    )
    @JvmField
    val DIMENSION: Codec<ResourceKey<World>> = KryptonResourceKey.codec(KryptonResourceKeys.WORLD)
    @JvmField
    val TAG_OR_ELEMENT_ID: Codec<TagOrElementLocation> = Codec.STRING.comapFlatMap(
        { input ->
            if (input.startsWith('#')) {
                Keys.read(input.substring(1)).map { TagOrElementLocation(it, true) }
            } else {
                Keys.read(input).map { TagOrElementLocation(it, false) }
            }
        },
        { it.decoratedId() }
    )
    @JvmField
    val BIT_SET: Codec<BitSet> = Codec.LONG_STREAM.xmap({ BitSet.valueOf(it.toArray()) }, { Arrays.stream(it.toLongArray()) })

    @JvmStatic
    fun fixedSize(stream: IntStream, size: Int): DataResult<IntArray> {
        val array = stream.limit((size + 1).toLong()).toArray()
        if (array.size != size) {
            val message = "Input is not a list of exactly $size integers!"
            return if (array.size >= size) DataResult.error(message, Arrays.copyOf(array, size)) else DataResult.error(message)
        }
        return DataResult.success(array)
    }

    @JvmStatic
    fun <E> stringResolver(toString: Function<E, String?>, fromString: Function<String, E?>): Codec<E> = Codec.STRING.flatXmap(
        { input -> Optional.ofNullable(fromString.apply(input)).successOrError { "Unknown element name $input!" } },
        { input -> Optional.ofNullable(toString.apply(input)).successOrError { "Element with unknown name $input!" } }
    )

    @JvmStatic
    fun <A> catchDecoderException(codec: Codec<A>): Codec<A> = Codec.of(codec, object : Decoder<A> {
        override fun <T> decode(input: T, ops: DataOps<T>): DataResult<Pair<A, T>> {
            return try {
                codec.decode(input, ops)
            } catch (exception: Exception) {
                DataResult.error("Caught exception decoding $codec: ${exception.message}")
            }
        }
    })

    @JvmStatic
    fun <E> retrieveContext(function: Function<DataOps<*>, DataResult<E>>): MapCodec<E> {
        class ContextRetrievalCodec : MapCodec<E> {
            override fun <T> encode(input: E, ops: DataOps<T>, prefix: RecordBuilder<T>): RecordBuilder<T> = prefix

            override fun <T> decode(input: MapLike<T>, ops: DataOps<T>): DataResult<E> = function.apply(ops)

            override fun toString(): String = "ContextRetrievalCodec[$function]"
        }
        return ContextRetrievalCodec()
    }

    @JvmStatic
    fun <E, L : Collection<E>, T> ensureHomogenous(toType: Function<E, T>): Function<L, DataResult<L>> = Function { values ->
        val iterator = values.iterator()
        if (iterator.hasNext()) {
            val firstType = toType.apply(iterator.next())
            while (iterator.hasNext()) {
                val next = iterator.next()
                val nextType = toType.apply(next)
                if (nextType !== firstType) {
                    return@Function DataResult.error("Mixed type list! Element $next had type $nextType, but list is of type $firstType!")
                }
            }
        }
        DataResult.success(values)
    }

    @JvmRecord
    data class TagOrElementLocation(val id: Key, val tag: Boolean) {

        fun decoratedId(): String = if (tag) "#${id.asString()}" else id.asString()

        override fun toString(): String = decoratedId()
    }
}
