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
package org.kryptonmc.krypton.world.region

import java.io.InputStream
import java.io.OutputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream

/**
 * Region file compression values, with their respective (de)compressors, so
 * they can be used to (de)compress given I/O streams in the [compress] and
 * [decompress] functions.
 */
enum class RegionFileCompression(
    private val compressor: (OutputStream) -> OutputStream,
    private val decompressor: (InputStream) -> InputStream
) {

    NONE({ it }, { it }),
    GZIP(::GZIPOutputStream, ::GZIPInputStream),
    ZLIB(::DeflaterOutputStream, ::InflaterInputStream);

    fun compress(output: OutputStream): OutputStream = compressor(output)

    fun decompress(input: InputStream): InputStream = decompressor(input)

    companion object {

        private val VALUES = values()

        @JvmStatic
        fun fromId(id: Byte): RegionFileCompression? = VALUES.getOrNull(id.toInt())
    }
}
