package org.kryptonmc.krypton.world.region

import java.io.InputStream
import java.io.OutputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream

enum class RegionFileCompression(
    private val compressor: (OutputStream) -> OutputStream,
    private val decompressor: (InputStream) -> InputStream
) {

    NONE({ it }, { it }),
    GZIP(::GZIPOutputStream, ::GZIPInputStream),
    ZLIB(::DeflaterOutputStream, ::InflaterInputStream);

    fun compress(output: OutputStream) = compressor(output)

    fun decompress(input: InputStream) = decompressor(input)

    companion object {

        fun fromId(id: Byte) = values().getOrNull(id.toInt())
    }
}