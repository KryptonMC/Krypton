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

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.effect.sound.SoundEvent
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
        { Arrays.stream(it.toIntArray()) }
    ).stable()
    val KEY: Codec<Key> = Codec.STRING.comapFlatMap({ parseKey(it) }, { it.asString() }).stable()
    val VECTOR3I_STREAM: Codec<Vector3i> = Codec.INT_STREAM.comapFlatMap(
        { stream -> stream.fixedSizeIntArray(3).map { Vector3i(it[0], it[1], it[2]) } },
        { IntStream.of(it.x(), it.y(), it.z()) }
    ).stable()
    val SOUND_EVENT: Codec<SoundEvent> = KEY.xmap(::SoundEvent) { it.key }.stable()
    val PARTICLE: Codec<Particle> = KEY.xmap({ InternalRegistries.PARTICLE_TYPE[it]!! }, { it.key }).stable()
    val DIMENSION: Codec<ResourceKey<World>> = KEY.xmap({ ResourceKey.of(ResourceKeys.DIMENSION, it) }, ResourceKey<World>::location).stable()

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

        override fun <T> encode(input: E, ops: DynamicOps<T>, prefix: T) =
            ops.mergeToPrimitive(prefix, if (ops.compressMaps()) ops.createInt(input.ordinal) else ops.createString(input.serialized))

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<E, T>> {
            if (ops.compressMaps()) return ops.getNumberValue(input)
                .flatMap { id -> values.getOrNull(id.toInt())?.let { DataResult.success(it) } ?: DataResult.error("Could not find any element with ID matching $id!") }
                .map { Pair.of(it, ops.empty()) }
            return ops.getStringValue(input)
                .flatMap { name -> nameToValue(name)?.let { DataResult.success(it) } ?: DataResult.error("Could not find any element with name matching $name!") }
                .map { Pair.of(it, ops.empty()) }
        }

        override fun toString() = "Enum & StringSerializable"
    }

    private fun parseKey(key: String) = try {
        DataResult.success(Key.key(key))
    } catch (exception: InvalidKeyException) {
        DataResult.error("$key is not a valid key! Exception: ${exception.message}")
    }
}

private fun IntStream.fixedSizeIntArray(size: Int): DataResult<IntArray> {
    val limited = limit(size + 1L).toArray()
    return when {
        limited.size == size -> DataResult.success(limited)
        limited.size >= size -> DataResult.error("Input is not an array of integers with size $size!", limited.copyOf(size))
        else -> DataResult.error("Input is not an array of integers with size $size!")
    }
}
