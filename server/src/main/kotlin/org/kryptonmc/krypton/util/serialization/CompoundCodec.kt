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

interface CompoundCodec<T> : Codec<CompoundTag, T>, CompoundEncoder<T>, CompoundDecoder<T> {

    private class Passthrough<T>(private val encoder: CompoundEncoder<T>, private val decoder: CompoundDecoder<T>) : CompoundCodec<T> {

        override fun encode(value: T): CompoundTag = encoder.encode(value)

        override fun decode(tag: CompoundTag): T = decoder.decode(tag)
    }

    companion object {

        @JvmStatic
        fun <T> of(encoder: CompoundEncoder<T>, decoder: CompoundDecoder<T>): CompoundCodec<T> = Passthrough(encoder, decoder)
    }
}

fun interface CompoundEncoder<T> : Encoder<T, CompoundTag>

fun interface CompoundDecoder<T> : Decoder<CompoundTag, T>
