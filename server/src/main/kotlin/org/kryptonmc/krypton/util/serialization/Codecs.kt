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
import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.spongepowered.math.vector.Vector3i
import java.util.UUID

object Codecs {

    @JvmField
    val BOOLEAN: Codec<ByteTag, Boolean> = Codec.of(ByteTag::of) { it.value != 0.toByte() }
    @JvmField
    val INTEGER: IntCodec<Int> = IntCodec.of({ it }, { it })
    @JvmField
    val LONG: Codec<LongTag, Long> = Codec.of(LongTag::of, LongTag::value)
    @JvmField
    val FLOAT: Codec<FloatTag, Float> = Codec.of(FloatTag::of, FloatTag::value)
    @JvmField
    val DOUBLE: Codec<DoubleTag, Double> = Codec.of(DoubleTag::of, DoubleTag::value)
    @JvmField
    val STRING: StringCodec<String> = StringCodec.of({ it }, { it })

    @JvmField
    val COLOR: IntCodec<Color> = IntCodec.of(Color::value, Color::of)
    @JvmField
    val UUID: IntArrayCodec<UUID> = IntArrayCodec.of(
        {
            val most = it.mostSignificantBits
            val least = it.leastSignificantBits
            intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt())
        },
        { it.fixedSize(4).toUUID() }
    )
    @JvmField
    val KEY: StringCodec<Key> = StringCodec.of(Key::asString, Key::key)
    @JvmField
    val VECTOR3I_ARRAY: IntArrayCodec<Vector3i> = IntArrayCodec.of(
        { intArrayOf(it.x(), it.y(), it.z()) },
        {
            val array = it.fixedSize(3)
            Vector3i(array[0], array[1], array[2])
        }
    )
    @JvmField
    val SOUND_EVENT: Codec<StringTag, SoundEvent> = KEY.transform(SoundEvent::key) { Registries.SOUND_EVENT[it]!! }
    @JvmField
    val PARTICLE: Codec<StringTag, ParticleType> = KEY.transform(ParticleType::key) { Registries.PARTICLE_TYPE[it]!! }
    @JvmField
    val DIMENSION: Codec<StringTag, ResourceKey<World>> = KEY.transform(ResourceKey<World>::location) { ResourceKey.of(ResourceKeys.DIMENSION, it) }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <K, V> map(keyCodec: StringCodec<K>, valueCodec: Codec<out Tag, V>): MapCodec<K, V> = MapCodec(keyCodec, valueCodec as Codec<Tag, V>)

    @JvmStatic
    fun <T> map(valueCodec: Codec<out Tag, T>): MapCodec<String, T> = map(STRING, valueCodec)

    @JvmStatic
    fun range(minimum: Int, maximum: Int): Codec<IntTag, Int> {
        val checker: (Int) -> Int = {
            require(it in minimum..maximum) { "Value $it is outside of expected range $minimum to $maximum!" }
            it
        }
        return INTEGER.transform(checker, checker)
    }

    @JvmStatic
    fun range(minimum: Double, maximum: Double): Codec<DoubleTag, Double> {
        val checker: (Double) -> Double = {
            require(it in minimum..maximum) { "Value $it is outside of expected range $minimum to $maximum!" }
            it
        }
        return DOUBLE.transform(checker, checker)
    }

    @JvmStatic
    fun <E : Keyed> forRegistry(registry: Registry<E>): StringCodec<E> =
        StringCodec.of({ it.key().value() }, { requireNotNull(registry[Key.key(it)]) { "Could not find any element with name matching $it!" } })
}

private fun IntArray.fixedSize(expected: Int): IntArray {
    if (size == expected) return this
    throw IllegalArgumentException("Input is not an array of integers with size $expected!")
}
