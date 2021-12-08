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
package org.kryptonmc.krypton.util.serialization

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag

fun <T> CompoundTag.Builder.encode(encoder: Encoder<T, out Tag>, name: String, value: T?): CompoundTag.Builder = apply {
    if (value != null) put(name, encoder.encode(value))
}

fun <T> CompoundTag.Builder.encode(encoder: CompoundEncoder<T>, value: T?): CompoundTag.Builder = apply {
    if (value != null) encoder.encode(value).forEach { put(it.key, it.value) }
}

@Suppress("UNCHECKED_CAST")
fun <T> CompoundTag.decode(decoder: Decoder<out Tag, T>, name: String): T? {
    val tag = get(name) ?: return null
    return (decoder as Decoder<Tag, T>).decodeNullable(tag)
}
