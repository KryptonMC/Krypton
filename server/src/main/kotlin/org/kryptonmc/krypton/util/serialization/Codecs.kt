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

import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.util.toIntArray
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.EitherCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.util.Either
import org.kryptonmc.util.Pair
import org.spongepowered.math.vector.Vector3i
import java.util.Arrays
import java.util.Objects
import java.util.UUID
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.IntStream

object Codecs {

    @JvmField
    val COLOR: Codec<Color> = Codec.INT.xmap(Color::of, Color::value)
    @JvmField
    val UUID: Codec<UUID> = Codec.INT_STREAM.xmap({ fixedSize(it, 4).toUUID() }, { Arrays.stream(it.toIntArray()) })
    @JvmField
    val KEY: Codec<Key> = Codec.STRING.xmap(Key::key, Key::asString)
    @JvmField
    val VECTOR3I_ARRAY: Codec<Vector3i> = Codec.INT_STREAM.xmap({
        val array = fixedSize(it, 3)
        Vector3i(array[0], array[1], array[2])
    }, { IntStream.of(it.x(), it.y(), it.z()) })
    @JvmField
    val SOUND_EVENT: Codec<SoundEvent> = KEY.xmap(::KryptonSoundEvent, SoundEvent::key)
    // TODO: Look at the particle type codec, since it's not that great here
    @JvmField
    val PARTICLE: Codec<ParticleType> = KEY.xmap({ Registries.PARTICLE_TYPE[it]!! }, ParticleType::key)
    @JvmField
    val DIMENSION: Codec<ResourceKey<World>> = KryptonResourceKey.codec(ResourceKeys.DIMENSION)

    @JvmStatic
    fun fixedSize(stream: IntStream, size: Int): IntArray {
        val array = stream.limit((size + 1).toLong()).toArray()
        check(array.size == size) { "Input is not an array of exactly $size integers! Array: $array, size: ${array.size}" }
        return array
    }

    @JvmStatic
    fun <E> fixedSize(list: List<E>, size: Int): List<E> {
        check(list.size == size) { "Input is not a list of exactly $size values! List: $list, size: ${list.size}" }
        return list
    }

    @JvmStatic
    fun <P : Any, I : Any> interval(
        elementCodec: Codec<P>,
        firstName: String,
        secondName: String,
        mapper: BiFunction<P, P, I>,
        firstGetter: Function<I, P>,
        secondGetter: Function<I, P>
    ): Codec<I> {
        val codec = Codec.list(elementCodec).xmap({
            val list = fixedSize(it, 2)
            mapper.apply(list[0], list[1])
        }, { persistentListOf(firstGetter.apply(it), secondGetter.apply(it)) })
        // Without the type arguments here, I think the compiler fails to infer what's going on, and produces the same error as
        // https://youtrack.jetbrains.com/issue/KT-53478
        @Suppress("RemoveExplicitTypeArguments")
        val fieldCodec: Codec<I> = RecordCodecBuilder.create<Pair<P, P>> { instance ->
            instance.group(
                elementCodec.field(firstName).getting(Pair<P, P>::first),
                elementCodec.field(secondName).getting(Pair<P, P>::second)
            ).apply(instance, ::Pair)
        }.xmap({ mapper.apply(it.first, it.second) }, { Pair.of(firstGetter.apply(it), secondGetter.apply(it)) })
        val eitherCodec = EitherCodec(codec, fieldCodec).xmap({ either -> either.map(Function.identity(), Function.identity()) }, { Either.left(it) })
        return Codec.either(elementCodec, eitherCodec).xmap({ either -> either.map({ mapper.apply(it, it) }, Function.identity()) }, {
            val first = firstGetter.apply(it)
            val second = secondGetter.apply(it)
            if (Objects.equals(first, second)) Either.left(first) else Either.right(it)
        })
    }

    @JvmStatic
    fun <E> stringResolver(toString: Function<E, String?>, fromString: Function<String, E?>): Codec<E> = Codec.STRING.xmap(
        { checkNotNull(fromString.apply(it)) { "Unknown element name $it!" } },
        { checkNotNull(toString.apply(it)) { "Element with unknown name $it!" } }
    )
}
