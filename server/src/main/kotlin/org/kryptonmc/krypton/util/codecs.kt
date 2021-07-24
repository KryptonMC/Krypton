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

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.api.util.getIfPresent
import org.kryptonmc.krypton.registry.RegistryFileCodec
import java.awt.Color
import java.util.Optional
import java.util.function.Function
import java.util.function.Supplier

val COLOR_CODEC: Codec<Color> = Codec.INT.xmap(::Color, Color::getRGB)

val NON_NEGATIVE_INT = intRange(0, Int.MAX_VALUE) { "Value $it must be non-negative!" }

private fun intRange(lower: Int, upper: Int, message: (Int) -> String): Codec<Int> {
    val rangeChecker = checkRange(lower, upper, message)
    return Codec.INT.flatXmap(rangeChecker, rangeChecker)
}

private fun <N> checkRange(lower: N, upper: N, message: (N) -> String): Function<N, DataResult<N>> where N : Number, N : Comparable<N> =
    Function { if (it in lower..upper) DataResult.success(it) else DataResult.error(message(it)) }

fun <T> nonNullSupplier() = Function<Supplier<T>, DataResult<Supplier<T>>> {
    try {
        if (it.get() == null) DataResult.error("Missing value $it!") else DataResult.success(it, Lifecycle.stable())
    } catch (exception: Exception) {
        DataResult.error("Invalid value: $it, message: ${exception.message}")
    }
}

fun <E : Any> homogenousListCodec(registryKey: ResourceKey<out Registry<E>>, elementCodec: Codec<E>): Codec<List<Supplier<E>>> = Codec.either(
    RegistryFileCodec(registryKey, elementCodec, false).listOf(),
    elementCodec.xmap({ Supplier { it } }, Supplier<E>::get).listOf()
).xmap({ either -> either.map({ it }, { it }) }, { Either.left(it) })

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
