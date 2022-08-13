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

import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.MapLike
import org.kryptonmc.serialization.RecordBuilder

@JvmRecord
data class NullableFieldCodec<A>(val name: String, val elementCodec: Codec<A>) : MapCodec<A?> {

    override fun <T> decode(input: MapLike<T>, ops: DataOps<T>): A? {
        val value = input.get(name) ?: return null
        return try {
            elementCodec.decode(value, ops)
        } catch (_: Exception) {
            null
        }
    }

    override fun <T : Any> encode(input: A?, ops: DataOps<T>, prefix: RecordBuilder<T>): RecordBuilder<T> {
        if (input != null) return prefix.add(name, elementCodec.encodeStart(input, ops))
        return prefix
    }

    override fun toString(): String = "NullableFieldCodec[$name: $elementCodec]"
}
