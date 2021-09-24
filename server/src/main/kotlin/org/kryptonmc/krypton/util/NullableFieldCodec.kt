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

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import java.util.stream.Stream

data class NullableFieldCodec<A>(
    private val name: String,
    private val elementCodec: Codec<A>
) : MapCodec<A?>() {

    override fun <T> decode(ops: DynamicOps<T>, input: MapLike<T>): DataResult<A?> {
        val value = input.get(name) ?: return DataResult.success(null)
        val parsed = elementCodec.parse(ops, value)
        if (parsed.result().isPresent) return parsed
        return DataResult.success(null)
    }

    override fun <T> encode(input: A?, ops: DynamicOps<T>, prefix: RecordBuilder<T>): RecordBuilder<T> {
        if (input != null) return prefix.add(name, elementCodec.encodeStart(ops, input))
        return prefix
    }

    override fun <T> keys(ops: DynamicOps<T>): Stream<T> = Stream.of(ops.createString(name))
}
