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

import org.kryptonmc.nbt.StringTag

interface StringCodec<T> : Codec<StringTag, T>, StringEncoder<T>, StringDecoder<T> {

    private class Passthrough<T>(private val encoder: StringEncoder<T>, private val decoder: StringDecoder<T>) : StringCodec<T> {

        override fun encodeString(value: T): String = encoder.encodeString(value)

        override fun decodeString(value: String): T = decoder.decodeString(value)
    }

    companion object {

        @JvmStatic
        fun <T> of(encoder: StringEncoder<T>, decoder: StringDecoder<T>): StringCodec<T> = Passthrough(encoder, decoder)
    }
}

fun interface StringEncoder<T> : Encoder<T, StringTag> {

    fun encodeString(value: T): String

    override fun encode(value: T): StringTag = StringTag.of(encodeString(value))
}

fun interface StringDecoder<T> : Decoder<StringTag, T> {

    fun decodeString(value: String): T

    override fun decode(tag: StringTag): T = decodeString(tag.value)
}
