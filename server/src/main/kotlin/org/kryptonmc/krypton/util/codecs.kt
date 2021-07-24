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
import com.mojang.serialization.MapCodec
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.api.util.getIfPresent
import java.awt.Color
import java.util.Optional
import java.util.function.IntFunction
import java.util.function.ToIntFunction

val COLOR_CODEC: Codec<Color> = Codec.INT.xmap(::Color, Color::getRGB)

fun <A> MapCodec<Optional<A>>.xmapOptional(): MapCodec<A?> = xmap({ it.getIfPresent() }, { Optional.ofNullable(it) })

fun <A> Codec<A>.nullableFieldOf(name: String) = optionalFieldOf(name).xmapOptional()

fun <E> Array<E>.codec(nameToValue: (String) -> E?): Codec<E> where E : Enum<E>, E : StringSerializable = object : Codec<E> {

    override fun <T> encode(input: E, ops: DynamicOps<T>, prefix: T) =
        ops.mergeToPrimitive(prefix, if (ops.compressMaps()) ops.createInt(input.ordinal) else ops.createString(input.serialized))

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<E, T>> = (if (ops.compressMaps()) ops.getNumberValue(input).flatMap { id ->
        this@codec.getOrNull(id.toInt())?.let { DataResult.success(it) } ?: DataResult.error("Unknown element with ID $id!")
    } else ops.getStringValue(input).flatMap { name ->
        nameToValue(name)?.let { DataResult.success(it) } ?: DataResult.error("Unknown element with name $name!")
    }).map { Pair.of(it, ops.empty()) }

    override fun toString() = "Enum & StringSerializable"
}
