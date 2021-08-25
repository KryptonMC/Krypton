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

import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.util.BitSet
import kotlin.io.path.moveTo
import kotlin.io.path.deleteIfExists
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.fileSize

/**
 * A region file holder, used for reading and writing region file data.
 *
 * This is heavily based on the original work from MCRegion, made by Scaevolous, and the modifications to it
 * made by Mojang AB, and also the modifications to make it work in Kotlin, and a few other optimisations, by
 * me, Callum Seabrook.
 */
class RegionFile(
    path: Path,
    synchronizeWrites: Boolean,
    private val externalDirectory: Path,
    private val compression: RegionFileCompression
) : AutoCloseable {

    private val channel: FileChannel
    private val header = ByteBuffer.allocateDirect(8192)
    private val offsets: IntBuffer
    private val timestamps: IntBuffer
    private val usedSectors = RegionBitmap()

    init {
        require(externalDirectory.isDirectory()) { "Expected directory, got ${externalDirectory.toAbsolutePath()}" }
        offsets = header.asIntBuffer().limit(1024)
        header.position(4096)
        timestamps = header.asIntBuffer()
        channel = FileChannel.open(path, if (synchronizeWrites) SHARED_FLAGS + StandardOpenOption.DSYNC else SHARED_FLAGS)
        usedSectors.force(0, 2)
        header.position(0)

        val first = channel.read(header, 0L)
        if (first != -1) {
            if (first != 8192) Messages.REGION.TRUNCATED.warn(LOGGER, path, first)
            for (i in 0 until 1024) {
                val offset = offsets[i]
                if (offset == 0) continue

                val sectorNumber = offset.sectorNumber
                val sectorCount = offset.sectorCount
                if (sectorNumber < 2) {
                    Messages.REGION.SECTOR.OVERLAP.warn(LOGGER, path, i, sectorNumber)
                    offsets[i] = 0
                    continue
                }
                if (sectorCount == 0) {
                    Messages.REGION.SECTOR.SIZE.warn(LOGGER, path, i)
                    offsets[i] = 0
                    continue
                }
                if (sectorNumber * 4096L > path.fileSize()) {
                    Messages.REGION.SECTOR.OUT_OF_BOUNDS.warn(LOGGER, path, i, sectorNumber)
                    offsets[i] = 0
                    continue
                }
                usedSectors.force(sectorNumber, sectorCount)
            }
        }
    }

    /**
     * Get a [DataInputStream] containing all of the chunk data for a chunk at the specified [position].
     *
     * @param position the position of the chunk to get data for
     * @return a [DataInputStream] containing the chunk data, or null if an error occurred
     */
    @Synchronized
    fun getChunkDataInputStream(position: ChunkPosition): DataInputStream? {
        val offset = offsets[position.offsetIndex]
        if (offset == 0) return null

        val sectorNumber = offset.sectorNumber
        val sectorCount = offset.sectorCount
        val size = sectorCount * 4096L

        val buffer = ByteBuffer.allocate(sectorCount * 4096)
        channel.read(buffer, sectorNumber * 4096L)
        buffer.flip()

        if (buffer.remaining() < 5) {
            Messages.REGION.CHUNK.TRUNCATED.error(LOGGER, position, size, buffer.remaining())
            return null
        }

        val length = buffer.int
        val compressionType = buffer.get()
        if (length == 0) {
            Messages.REGION.CHUNK.NO_STREAM.warn(LOGGER, position)
            return null
        }

        val dataLength = length - 1
        if (compressionType.isExternalStreamChunk) {
            if (dataLength != 0) Messages.REGION.CHUNK.INTERNAL_EXTERNAL.warn(LOGGER)
            return createExternalChunkInputStream(position, compressionType.externalChunkVersion)
        }
        if (dataLength > buffer.remaining()) {
            Messages.REGION.CHUNK.STREAM_TRUNCATED.error(LOGGER, position, dataLength, buffer.remaining())
            return null
        }
        if (dataLength < 0) {
            Messages.REGION.CHUNK.NEGATIVE.error(LOGGER, length, position)
            return null
        }
        return createChunkInputStream(position, compressionType, ByteArrayInputStream(buffer.array(), buffer.position(), dataLength))
    }

    /**
     * Gets a [DataOutputStream] for writing chunk data to
     *
     * @param position the position of the chunk
     * @return a [DataOutputStream] for writing chunk data to
     */
    fun getChunkDataOutputStream(position: ChunkPosition) = DataOutputStream(BufferedOutputStream(compression.compress(ChunkBuffer(position))))

    /**
     * Writes chunk data for a chunk at the specified [position]
     *
     * @param position the position of the chunk to write
     * @param buffer the data to write
     */
    @Synchronized
    fun write(position: ChunkPosition, buffer: ByteBuffer) {
        val action: () -> Unit
        val sectors: Int

        val offsetIndex = position.offsetIndex
        val offset = offsets[offsetIndex]
        val sectorNumber = offset.sectorNumber
        val sectorCount = offset.sectorCount
        val length = buffer.remaining()
        var requiredSectors = (length + 4096 - 1) / 4096

        if (requiredSectors >= 256) {
            val path = externalDirectory.resolve("c.${position.x}.${position.z}.mcc")
            Messages.REGION.CHUNK.EXTERNAL.SAVE.warn(LOGGER, position, length, path)
            requiredSectors = 1
            sectors = usedSectors.allocate(length)
            action = writeExternal(path, buffer)
            val externalStub = createExternalStub()
            channel.write(externalStub, sectors * 4096L)
        } else {
            sectors = usedSectors.allocate(requiredSectors)
            action = { externalDirectory.resolve("c.${position.x}.${position.z}.mcc").deleteIfExists() }
            channel.write(buffer, sectors * 4096L)
        }
        val timestamp = (Instant.now().toEpochMilli() / 1000).toInt()
        offsets[offsetIndex] = sectors shl 8 or requiredSectors
        timestamps[offsetIndex] = timestamp
        writeHeader()
        action()
        if (sectorNumber != 0) usedSectors.free(sectorNumber, sectorCount)
    }

    private fun createExternalChunkInputStream(position: ChunkPosition, compressionType: Byte): DataInputStream? {
        val path = externalDirectory.resolve("c.${position.x}.${position.z}.mcc")
        if (!path.isRegularFile()) {
            Messages.REGION.CHUNK.EXTERNAL.NOT_FILE.error(LOGGER, path)
            return null
        }
        return createChunkInputStream(position, compressionType, path.inputStream())
    }

    private fun createChunkInputStream(position: ChunkPosition, compressionType: Byte, input: InputStream): DataInputStream? {
        val compression = RegionFileCompression.fromId(compressionType)
        if (compression == null) {
            Messages.REGION.CHUNK.INVALID_COMPRESSION_TYPE.error(LOGGER, position, compressionType)
            return null
        }
        return DataInputStream(BufferedInputStream(compression.decompress(input)))
    }

    private fun writeExternal(path: Path, buffer: ByteBuffer): () -> Unit {
        val temp = externalDirectory.createTempFile("tmp")
        FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            buffer.position(5)
            it.write(buffer)
        }
        return { temp.moveTo(path, StandardCopyOption.REPLACE_EXISTING) }
    }

    private fun writeHeader() {
        header.position(0)
        channel.write(header, 0)
    }

    private fun createExternalStub() =
        ByteBuffer.allocate(5).putInt(1).put((compression.ordinal or 0x80).toByte()).flip()

    private fun padToFullSector() {
        val size = channel.size()
        val sectors = size + 4096 - 1
        if (size != sectors) {
            val buffer = PADDING_BUFFER.duplicate()
            buffer.position(0)
            channel.write(buffer, sectors - 1)
        }
    }

    fun flush() = channel.force(true)

    override fun close() = try {
        padToFullSector()
    } finally {
        channel.use { it.force(true) }
    }

    private inner class ChunkBuffer(private val position: ChunkPosition) : ByteArrayOutputStream(8096) {

        init {
            repeat(4) { write(0) }
            write(this@RegionFile.compression.ordinal)
        }

        override fun close() {
            val buffer = ByteBuffer.wrap(buf, 0, count)
            buffer.putInt(0, count - 5 + 1)
            this@RegionFile.write(position, buffer)
        }
    }

    private class RegionBitmap {

        private val used = BitSet()

        fun force(from: Int, to: Int) = used.set(from, from + to)

        fun free(from: Int, to: Int) = used.clear(from, from + to)

        fun allocate(bits: Int): Int {
            var i = 0
            while (true) {
                val nextClearBit = used.nextClearBit(i)
                val nextSetBit = used.nextSetBit(nextClearBit)
                if (nextSetBit == -1 || nextSetBit - nextClearBit >= bits) return nextClearBit.apply { force(this, bits) }
                i = nextSetBit
            }
        }
    }

    companion object {

        private val LOGGER = logger<RegionFile>()
        private val SHARED_FLAGS = setOf(
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
        )
        private val PADDING_BUFFER = ByteBuffer.allocateDirect(1)

        private val Int.sectorNumber: Int
            get() = shr(8) and 0xFFFFFF

        private val Int.sectorCount: Int
            get() = and(0xFF)

        private val ChunkPosition.offsetIndex: Int
            get() = (x and 0x1F) + (z and 0x1F) * 32

        private val Byte.isExternalStreamChunk: Boolean
            get() = toInt() and 0x80 != 0

        private val Byte.externalChunkVersion: Byte
            get() = (toInt() and 0xFFFFFF7F.toInt()).toByte()

        private operator fun IntBuffer.set(index: Int, value: Int): IntBuffer = put(index, value)
    }
}
