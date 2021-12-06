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
package org.kryptonmc.krypton.util

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.registry.InternalRegistries
import org.spongepowered.math.vector.Vector3i
import java.awt.Color
import java.util.Arrays
import java.util.UUID
import java.util.stream.IntStream

object Codecs {

    val COLOR: Codec<Color> = Codec.INT.xmap(::Color, Color::getRGB).stable()
    val NON_NEGATIVE_INT: Codec<Int> = Codec.INT.flatXmap(
        { if (it >= 0) DataResult.success(it) else DataResult.error("Value $it must be non-negative!") },
        { if (it >= 0) DataResult.success(it) else DataResult.error("Value $it must be non-negative!") }
    ).stable()
    val UUID: Codec<UUID> = Codec.INT_STREAM.comapFlatMap(
        { it.fixedSizeIntArray(4).map(IntArray::toUUID) },
        {
            val most = it.mostSignificantBits
            val least = it.leastSignificantBits
            Arrays.stream(intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt()))
        }
    ).stable()
    val KEY: Codec<Key> = Codec.STRING.comapFlatMap({ parseKey(it) }, { it.asString() }).stable()
    val VECTOR3I_STREAM: Codec<Vector3i> = Codec.INT_STREAM.comapFlatMap(
        { stream -> stream.fixedSizeIntArray(3).map { Vector3i(it[0], it[1], it[2]) } },
        { IntStream.of(it.x(), it.y(), it.z()) }
    ).stable()
    val SOUND_EVENT: Codec<SoundEvent> = KEY.xmap({ InternalRegistries.SOUND_EVENT[it]!! }, { it.key() }).stable()
    val PARTICLE: Codec<ParticleType> = KEY.xmap({ InternalRegistries.PARTICLE_TYPE[it]!! }, { it.key() }).stable()
    val DIMENSION: Codec<ResourceKey<World>> = KEY.xmap(
        { ResourceKey.of(ResourceKeys.DIMENSION, it) },
        ResourceKey<World>::location
    ).stable()

    /**
     * Creates an enum codec that supports both integer and string values.
     *
     * @param values the enum values
     * @param nameToValue a function that takes a name and optionally returns an enum
     * constant with that name
     */
    @JvmStatic
    inline fun <E> forEnum(
        values: Array<E>,
        crossinline nameToValue: (String) -> E?
    ): Codec<E> where E : Enum<E>, E : StringSerializable = object : Codec<E> {

        override fun <T> encode(input: E, ops: DynamicOps<T>, prefix: T): DataResult<T> {
            if (ops.compressMaps()) return ops.mergeToPrimitive(prefix, ops.createInt(input.ordinal))
            return ops.mergeToPrimitive(prefix, ops.createString(input.serialized))
        }

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<E, T>> {
            if (ops.compressMaps()) return ops.getNumberValue(input)
                .flatMap { id -> values.getOrNull(id.toInt())?.successOrError("Could not find any element with ID matching $id!") }
                .map { Pair.of(it, ops.empty()) }
            return ops.getStringValue(input)
                .flatMap { name -> nameToValue(name)?.successOrError("Could not find any element with name matching $name!") }
                .map { Pair.of(it, ops.empty()) }
        }

        override fun toString() = "Enum & StringSerializable"
    }

    @JvmStatic
    inline fun <P : Any, I> interval(
        elementCodec: Codec<P>,
        firstName: String,
        secondName: String,
        noinline constructor: (P, P) -> DataResult<I>,
        crossinline extractFirst: (I) -> P,
        crossinline extractSecond: (I) -> P
    ): Codec<I> {
        val codec = Codec.list(elementCodec).comapFlatMap({ list ->
            list.fixedSize(2).flatMap {
                val first = it[0]
                val second = it[1]
                constructor(first, second)
            }
        }, { ImmutableList.of(extractFirst(it), extractSecond(it)) })
        val otherCodec = RecordCodecBuilder.create<Pair<P, P>> {
            it.group(
                elementCodec.fieldOf(firstName).forGetter(Pair<P, P>::getFirst),
                elementCodec.fieldOf(secondName).forGetter(Pair<P, P>::getSecond)
            ).apply(it) { first, second -> Pair.of(first, second) }
        }.comapFlatMap({ constructor(it.first, it.second) }, { Pair.of(extractFirst(it), extractSecond(it)) })
        val eitherCodec = EitherCodec(codec, otherCodec).xmap(
            { either -> either.map({ it }, { it }) },
            { Either.left(it) }
        )
        return Codec.either(elementCodec, eitherCodec).comapFlatMap(
            { either -> either.map({ constructor(it, it) }, { DataResult.success(it) }) },
            {
                val min = extractFirst(it)
                val max = extractSecond(it)
                if (min == max) Either.left(min) else Either.right(it)
            }
        )
    }

    @JvmStatic
    fun <E : Keyed> forRegistry(registry: Registry<E>): Codec<E> = object : Codec<E> {

        override fun <T> encode(input: E, ops: DynamicOps<T>, prefix: T): DataResult<T> {
            if (ops.compressMaps()) return ops.mergeToPrimitive(prefix, ops.createInt(registry.idOf(input)))
            return ops.mergeToPrimitive(prefix, ops.createString(input.key().value()))
        }

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<E, T>> {
            if (ops.compressMaps()) return ops.getNumberValue(input)
                .flatMap { id -> registry[id.toInt()]?.successOrError("Could not find any element with ID matching $id!") }
                .map { Pair.of(it, ops.empty()) }
            return ops.getStringValue(input)
                .flatMap { name -> registry[Key.key(name)]?.successOrError("Could not find any element with name matching $name!") }
                .map { Pair.of(it, ops.empty()) }
        }
    }

    @JvmStatic
    private fun parseKey(key: String) = try {
        DataResult.success(Key.key(key))
    } catch (exception: InvalidKeyException) {
        DataResult.error("$key is not a valid key! Exception: ${exception.message}")
    }

    @JvmStatic
    private fun IntStream.fixedSizeIntArray(size: Int): DataResult<IntArray> {
        val limited = limit(size + 1L).toArray()
        return when {
            limited.size == size -> DataResult.success(limited)
            limited.size >= size -> DataResult.error(
                "Input is not an array of integers with size $size!",
                limited.copyOf(size)
            )
            else -> DataResult.error("Input is not an array of integers with size $size!")
        }
    }

    @JvmStatic
    @PublishedApi
    internal fun <T> List<T>.fixedSize(size: Int): DataResult<List<T>> {
        if (this.size != size) {
            val errorMessage = "Input is not a list of exactly $size elements, was a list of ${this.size} elements!"
            return if (this.size >= size) DataResult.error(errorMessage, subList(0, size)) else DataResult.error(errorMessage)
        }
        return DataResult.success(this)
    }

    class EitherCodec<F, S>(private val first: Codec<F>, private val second: Codec<S>) : Codec<Either<F, S>> {

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Either<F, S>, T>> {
            val firstDecode = first.decode(ops, input).map { pair -> pair.mapFirst { Either.left<F, S>(it) } }
            if (!firstDecode.error().isPresent) return firstDecode
            val secondDecode = second.decode(ops, input).map { pair -> pair.mapFirst { Either.right<F, S>(it) } }
            if (!secondDecode.error().isPresent) return secondDecode
            return firstDecode.apply2({ _, second -> second }, secondDecode)
        }

        override fun <T> encode(input: Either<F, S>, ops: DynamicOps<T>, prefix: T): DataResult<T> = input.map(
            { first.encode(it, ops, prefix) },
            { second.encode(it, ops, prefix) }
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as EitherCodec<*, *>
            return first == other.first && second == other.second
        }

        override fun hashCode(): Int {
            var result = 1
            result = 31 * result + first.hashCode()
            result = 31 * result + second.hashCode()
            return result
        }

        override fun toString(): String = "EitherCodec(first=$first, second=$second)"
    }
}
