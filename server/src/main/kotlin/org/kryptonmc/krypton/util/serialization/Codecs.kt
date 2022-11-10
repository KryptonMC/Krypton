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

import com.google.common.collect.ImmutableList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.mapSuccess
import org.kryptonmc.krypton.util.orElseError
import org.kryptonmc.krypton.util.toIntArray
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.Lifecycle
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.util.Either
import org.kryptonmc.util.Pair
import java.util.Arrays
import java.util.Objects
import java.util.Optional
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.IntStream

object Codecs {

    @JvmField
    val COLOR: Codec<Color> = Codec.INT.xmap(Color::of, Color::value)
    @JvmField
    val UUID: Codec<UUID> = Codec.INT_STREAM.comapFlatMap({ fixedSize(it, 4).map(IntArray::toUUID) }, { Arrays.stream(it.toIntArray()) })
    @JvmField
    val KEY: Codec<Key> = Codec.STRING.comapFlatMap(Keys::read, Key::asString).stable()
    @JvmField
    val SOUND_EVENT: Codec<SoundEvent> = KEY.xmap({ KryptonSoundEvent(it, 16F) }, SoundEvent::key)
    // TODO: Look at the particle type codec, since it's not that great here
    @JvmField
    val PARTICLE: Codec<ParticleType> = KEY.xmap({ KryptonRegistries.PARTICLE_TYPE.get(it)!! }, { KryptonRegistries.PARTICLE_TYPE.getKey(it)!! })
    @JvmField
    val DIMENSION: Codec<ResourceKey<World>> = KryptonResourceKey.codec(ResourceKeys.DIMENSION)

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
    fun <E> fixedSize(list: List<E>, size: Int): DataResult<List<E>> {
        if (list.size != size) {
            val message = "Input is not a list of exactly $size elements!"
            return if (list.size >= size) DataResult.error(message, list.subList(0, size)) else DataResult.error(message)
        }
        return DataResult.success(list)
    }

    @JvmStatic
    fun <P : Any, I : Any> interval(
        elementCodec: Codec<P>,
        firstName: String,
        secondName: String,
        mapper: BiFunction<P, P, DataResult<I>>,
        firstGetter: Function<I, P>,
        secondGetter: Function<I, P>
    ): Codec<I> {
        val codec = Codec.list(elementCodec).comapFlatMap(
            { input -> fixedSize(input, 2).flatMap { mapper.apply(it[0], it[1]) } },
            { ImmutableList.of(firstGetter.apply(it), secondGetter.apply(it)) }
        )
        // Without the type arguments here, I think the compiler fails to infer what's going on, and produces the same error as
        // https://youtrack.jetbrains.com/issue/KT-53478
        @Suppress("RemoveExplicitTypeArguments")
        val fieldCodec: Codec<I> = RecordCodecBuilder.create<Pair<P, P>> { instance ->
            instance.group(
                elementCodec.fieldOf(firstName).getting(Pair<P, P>::first),
                elementCodec.fieldOf(secondName).getting(Pair<P, P>::second)
            ).apply(instance, ::Pair)
        }.comapFlatMap({ mapper.apply(it.first, it.second) }, { Pair.of(firstGetter.apply(it), secondGetter.apply(it)) })
        val eitherCodec = EitherCodec(codec, fieldCodec).xmap({ it.map(Function.identity(), Function.identity()) }, { Either.left(it) })
        return Codec.either(elementCodec, eitherCodec).comapFlatMap({ value -> value.map({ mapper.apply(it, it) }, { DataResult.success(it) }) }, {
            val first = firstGetter.apply(it)
            val second = secondGetter.apply(it)
            if (Objects.equals(first, second)) Either.left(first) else Either.right(it)
        })
    }

    @JvmStatic
    fun <A> orElsePartial(partialValue: A): Codec.ResultFunction<A> = object : Codec.ResultFunction<A> {
        override fun <T> apply(input: T, ops: DataOps<T>, result: DataResult<Pair<A, T>>): DataResult<Pair<A, T>> {
            val value = AtomicReference<String>()
            val decoded = result.resultOrPartial(value::setPlain)
            return if (decoded.isPresent) result else DataResult.error("(${value.plain} -> using default)", Pair.of(partialValue, input))
        }

        override fun <T> coApply(input: A, ops: DataOps<T>, result: DataResult<T>): DataResult<T> = result

        override fun toString(): String = "OrElsePartial[$partialValue]"
    }

    @JvmStatic
    fun <E> stringResolver(toString: Function<E, String?>, fromString: Function<String, E?>): Codec<E> = Codec.STRING.flatXmap(
        { input -> Optional.ofNullable(fromString.apply(input)).mapSuccess().orElseError("Unknown element name $input!") },
        { input -> Optional.ofNullable(toString.apply(input)).mapSuccess().orElseError("Element with unknown name $input!") }
    )

    @JvmStatic
    fun <E> overrideLifecycle(
        codec: Codec<E>,
        decodeLifecycle: Function<E, Lifecycle>,
        encodeLifecycle: Function<E, Lifecycle>
    ): Codec<E> = codec.mapResult(object : Codec.ResultFunction<E> {
        override fun <T> apply(input: T, ops: DataOps<T>, result: DataResult<Pair<E, T>>): DataResult<Pair<E, T>> =
            result.result().map { result.withLifecycle(decodeLifecycle.apply(it.first)) }.orElse(result)

        override fun <T> coApply(input: E, ops: DataOps<T>, result: DataResult<T>): DataResult<T> =
            result.withLifecycle(encodeLifecycle.apply(input))

        override fun toString(): String = "WithLifecycle[$decodeLifecycle $encodeLifecycle]"
    })

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

    private class EitherCodec<L : Any, R : Any>(private val left: Codec<L>, private val right: Codec<R>) : Codec<Either<L, R>> {

        override fun <T> decode(input: T, ops: DataOps<T>): DataResult<Pair<Either<L, R>, T>> {
            val leftRead = left.decode(input, ops).map { result -> result.mapFirst { Either.left<L, R>(it) } }
            if (!leftRead.error().isPresent) return leftRead
            val rightRead = right.decode(input, ops).map { result -> result.mapFirst { Either.right<L, R>(it) } }
            return if (!rightRead.error().isPresent) rightRead else leftRead.apply2({ _, r2 -> r2 }, rightRead)
        }

        override fun <T : Any> encode(input: Either<L, R>, ops: DataOps<T>, prefix: T): DataResult<T> =
            input.map({ left.encode(it, ops, prefix) }, { right.encode(it, ops, prefix) })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            return Objects.equals(left, (other as EitherCodec<*, *>).left) && Objects.equals(right, other.right)
        }

        override fun hashCode(): Int = Objects.hash(left, right)

        override fun toString(): String = "EitherCodec[$left, $right]"
    }
}
