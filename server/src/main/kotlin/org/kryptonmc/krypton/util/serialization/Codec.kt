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

import org.kryptonmc.nbt.Tag

interface Codec<T : Tag, U> : Encoder<U, T>, Decoder<T, U> {

    fun <V> transform(encodeTransformer: (V) -> U, decodeTransformer: (U) -> V): TransformingCodec<T, U, V> =
        TransformingCodec(this, encodeTransformer, decodeTransformer)

    @Suppress("UNCHECKED_CAST")
    fun list(): ListCodec<U> = ListCodec(this as Codec<Tag, U>)

    private class Passthrough<T : Tag, U>(private val encoder: Encoder<U, T>, private val decoder: Decoder<T, U>) : Codec<T, U> {

        override fun encode(value: U): T = encoder.encode(value)

        override fun decode(tag: T): U = decoder.decode(tag)
    }

    companion object {

        @JvmStatic
        fun <T : Tag, U> of(encoder: Encoder<U, T>, decoder: Decoder<T, U>): Codec<T, U> = Passthrough(encoder, decoder)
    }
}
