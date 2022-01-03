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

import org.kryptonmc.nbt.IntArrayTag

interface IntArrayCodec<T> : Codec<IntArrayTag, T>, IntArrayEncoder<T>, IntArrayDecoder<T> {

    private class Passthrough<T>(private val encoder: IntArrayEncoder<T>, private val decoder: IntArrayDecoder<T>) : IntArrayCodec<T> {

        override fun encodeIntArray(value: T): IntArray = encoder.encodeIntArray(value)

        override fun decodeIntArray(array: IntArray): T = decoder.decodeIntArray(array)
    }

    companion object {

        @JvmStatic
        fun <T> of(encoder: IntArrayEncoder<T>, decoder: IntArrayDecoder<T>): IntArrayCodec<T> = Passthrough(encoder, decoder)
    }
}

fun interface IntArrayEncoder<I> : Encoder<I, IntArrayTag> {

    fun encodeIntArray(value: I): IntArray

    override fun encode(value: I): IntArrayTag = IntArrayTag(encodeIntArray(value))
}

fun interface IntArrayDecoder<O> : Decoder<IntArrayTag, O> {

    fun decodeIntArray(array: IntArray): O

    override fun decode(tag: IntArrayTag): O = decodeIntArray(tag.data)
}
