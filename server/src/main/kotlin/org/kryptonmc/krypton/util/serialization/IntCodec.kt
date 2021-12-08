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

import org.kryptonmc.nbt.IntTag

interface IntCodec<T> : Codec<IntTag, T>, IntEncoder<T>, IntDecoder<T> {

    private class Passthrough<T>(private val encoder: IntEncoder<T>, private val decoder: IntDecoder<T>) : IntCodec<T> {

        override fun encodeInt(value: T): Int = encoder.encodeInt(value)

        override fun decodeInt(value: Int): T = decoder.decodeInt(value)
    }

    companion object {

        @JvmStatic
        fun <T> of(encoder: IntEncoder<T>, decoder: IntDecoder<T>): IntCodec<T> = Passthrough(encoder, decoder)
    }
}

fun interface IntEncoder<I> : Encoder<I, IntTag> {

    fun encodeInt(value: I): Int

    override fun encode(value: I): IntTag = IntTag.of(encodeInt(value))
}

fun interface IntDecoder<O> : Decoder<IntTag, O> {

    fun decodeInt(value: Int): O

    override fun decode(tag: IntTag): O = decodeInt(tag.value)
}
