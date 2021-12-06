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

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.util.FastBufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream

class RegionFileVersion private constructor(
    val id: Int,
    private val decompressor: Converter<InputStream>,
    private val compressor: Converter<OutputStream>
) {

    fun compress(output: OutputStream): OutputStream = compressor.convert(output)

    fun decompress(input: InputStream): InputStream = decompressor.convert(input)

    private fun interface Converter<T> {

        fun convert(stream: T): T
    }

    companion object {

        private val BY_ID = Int2ObjectOpenHashMap<RegionFileVersion>()

        @JvmField
        val GZIP: RegionFileVersion = register(RegionFileVersion(
            1,
            { FastBufferedInputStream(GZIPInputStream(it)) },
            { BufferedOutputStream(GZIPOutputStream(it)) }
        ))
        @JvmField
        val ZLIB: RegionFileVersion = register(RegionFileVersion(
            2,
            { FastBufferedInputStream(InflaterInputStream(it)) },
            { BufferedOutputStream(DeflaterOutputStream(it)) }
        ))
        @JvmField
        val NONE: RegionFileVersion = register(RegionFileVersion(3, { it }, { it }))

        @JvmStatic
        fun fromId(id: Int): RegionFileVersion? = BY_ID[id]

        @JvmStatic
        fun isValid(id: Int): Boolean = BY_ID.containsKey(id)

        @JvmStatic
        private fun register(version: RegionFileVersion): RegionFileVersion = version.apply { BY_ID[version.id] = version }
    }
}
