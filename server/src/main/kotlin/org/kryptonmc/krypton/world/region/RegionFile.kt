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

import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.util.BitSet
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

/**
 * A region file holder, used for reading and writing region file data.
 *
 * This is heavily based on the original work from MCRegion, made by
 * Scaevolous, and the modifications to it made by Mojang AB, and also the
 * modifications to make it work in Kotlin, and a few other optimisations, by
 * KryptonMC.
 */
class RegionFile(
    private val folder: Path,
    private val externalFolder: Path,
    private val version: RegionFileVersion,
    synchronizeWrites: Boolean,
) : AutoCloseable {

    private val channel: FileChannel
    private val header = ByteBuffer.allocateDirect(SECTOR_BYTES * 2)
    private val offsets: IntBuffer
    private val timestamps: IntBuffer
    private val usedSectors = RegionBitmap()

    init {
        require(externalFolder.isDirectory()) { "Expected directory, got ${externalFolder.toAbsolutePath()}" }

        offsets = header.asIntBuffer().limit(SECTOR_INTS)
        header.position(SECTOR_BYTES)
        timestamps = header.asIntBuffer()
        channel = if (synchronizeWrites) FileChannel.open(folder, DSYNC_FLAGS) else FileChannel.open(folder, STANDARD_FLAGS)

        usedSectors.force(0, 2)
        header.position(HEADER_OFFSET)

        val first = channel.read(header, HEADER_OFFSET.toLong())
        if (first != -1) {
            if (first != SECTOR_BYTES * 2) LOGGER.error("Region file $folder has truncated header! Expected ${SECTOR_BYTES * 2} bytes, was $first")

            val fileSize = Files.size(folder)
            for (i in 0 until SECTOR_INTS) {
                val offset = offsets[i]
                if (offset == 0) continue

                val sectorNumber = sectorNumber(offset)
                val sectorCount = sectorCount(offset)
                if (sectorNumber < 2) {
                    LOGGER.error("Region file $folder has an invalid sector at index $i! Sector $sectorNumber overlaps with header!")
                    offsets.put(i, CHUNK_NOT_PRESENT)
                    continue
                }
                if (sectorCount == 0) {
                    LOGGER.error("Region file $folder has an invalid sector at index $i! Sector count was less than 1!")
                    offsets.put(i, CHUNK_NOT_PRESENT)
                    continue
                }
                if (sectorNumber * SECTOR_BYTES > fileSize) {
                    LOGGER.error("Region file $folder has an invalid sector at index $i! Sector $sectorNumber is out of bounds!")
                    offsets.put(i, CHUNK_NOT_PRESENT)
                    continue
                }
                usedSectors.force(sectorNumber, sectorCount)
            }
        }
    }

    constructor(
        folder: Path,
        externalFolder: Path,
        synchronizeWrites: Boolean
    ) : this(folder, externalFolder, RegionFileVersion.ZLIB, synchronizeWrites)

    /**
     * Get a [DataInputStream] containing all of the chunk data for a chunk at the specified [position].
     *
     * @param position the position of the chunk to get data for
     * @return a [DataInputStream] containing the chunk data, or null if an error occurred
     */
    @Synchronized
    fun getChunkDataInputStream(position: ChunkPosition): DataInputStream? {
        val offset = offset(position)
        if (offset == 0) return null

        val sectorNumber = sectorNumber(offset)
        val sectorCount = sectorCount(offset)
        val size = sectorCount * SECTOR_BYTES

        val buffer = ByteBuffer.allocate(sectorCount * SECTOR_BYTES)
        channel.read(buffer, (sectorNumber * SECTOR_BYTES).toLong())
        buffer.flip()

        if (buffer.remaining() < 5) {
            LOGGER.error("Chunk at $position in region file $folder has truncated header! Expected $size, but was ${buffer.remaining()}!")
            return null
        }

        val length = buffer.int
        val compressionType = buffer.get()
        if (length == 0) {
            LOGGER.error("Chunk at $position in region file $folder is allocated, but has no stream!")
            return null
        }

        val dataLength = length - 1
        if (isExternalStreamChunk(compressionType)) {
            if (dataLength != 0) {
                LOGGER.error("Chunk at $position in region file $folder has both internal and external streams!")
            }
            return createExternalChunkInputStream(position, externalChunkVersion(compressionType))
        }
        if (dataLength > buffer.remaining()) {
            LOGGER.error("Chunk at $position in region file $folder has a truncated stream! Expected $dataLength, but was ${buffer.remaining()}!")
            return null
        }
        if (dataLength < 0) {
            LOGGER.error("Chunk at $position in region file $folder has a negative declared size!")
            return null
        }
        return createChunkInputStream(position, compressionType, createStream(buffer, dataLength))
    }

    /**
     * Gets a [DataOutputStream] for writing chunk data to
     *
     * @param position the position of the chunk
     * @return a [DataOutputStream] for writing chunk data to
     */
    fun getChunkDataOutputStream(position: ChunkPosition): DataOutputStream = DataOutputStream(version.compress(ChunkBuffer(position)))

    /**
     * Writes chunk data for a chunk at the specified [position]
     *
     * @param position the position of the chunk to write
     * @param buffer the data to write
     */
    @Synchronized
    fun write(position: ChunkPosition, buffer: ByteBuffer) {
        val action: Runnable
        val sectors: Int

        val offsetIndex = offsetIndex(position)
        val offset = offsets.get(offsetIndex)
        val sectorNumber = sectorNumber(offset)
        val sectorCount = sectorCount(offset)
        val length = buffer.remaining()
        var requiredSectors = sectorCountFromSize(length)

        if (requiredSectors >= EXTERNAL_CHUNK_THRESHOLD) {
            val path = resolveExternalChunkPath(position)
            LOGGER.warn("Saving oversized chunk at $position in file $path, due to it being $length bytes.")
            requiredSectors = 1
            sectors = usedSectors.allocate(requiredSectors)
            action = writeExternal(path, buffer)
            val externalStub = createExternalStub()
            channel.write(externalStub, (sectors * SECTOR_BYTES).toLong())
        } else {
            sectors = usedSectors.allocate(requiredSectors)
            action = Runnable { Files.deleteIfExists(resolveExternalChunkPath(position)) }
            channel.write(buffer, (sectors * SECTOR_BYTES).toLong())
        }

        offsets.put(offsetIndex, packSectorOffset(sectors, requiredSectors))
        timestamps.put(offsetIndex, timestamp())
        writeHeader()
        action.run()
        if (sectorNumber != 0) usedSectors.free(sectorNumber, sectorCount)
    }

    private fun createExternalChunkInputStream(position: ChunkPosition, compressionType: Byte): DataInputStream? {
        val path = resolveExternalChunkPath(position)
        if (!path.isRegularFile()) {
            LOGGER.error("External path $path for chunk at $position is not a file!")
            return null
        }
        return createChunkInputStream(position, compressionType, path.inputStream())
    }

    private fun createChunkInputStream(position: ChunkPosition, compressionType: Byte, input: InputStream): DataInputStream? {
        val compression = RegionFileVersion.fromId(compressionType.toInt())
        if (compression == null) {
            LOGGER.error("Chunk at $position in region file $folder has an invalid compression type! Type: $compressionType")
            return null
        }
        return DataInputStream(compression.decompress(input))
    }

    private fun writeExternal(path: Path, buffer: ByteBuffer): Runnable {
        val tempFile = Files.createTempFile(externalFolder, "tmp", null)
        val channel = FileChannel.open(tempFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)

        try {
            buffer.position(CHUNK_HEADER_SIZE)
            channel.write(buffer)
        } catch (exception: Throwable) {
            if (channel != null) {
                try {
                    channel.close()
                } catch (closeException: Exception) {
                    exception.addSuppressed(closeException)
                }
            }
            throw exception
        }
        channel?.close()
        return Runnable { Files.move(tempFile, path, StandardCopyOption.REPLACE_EXISTING) }
    }

    private fun offset(position: ChunkPosition): Int = offsets.get(offsetIndex(position))

    private fun resolveExternalChunkPath(position: ChunkPosition): Path {
        val name = "c.${position.x}.${position.z}$EXTERNAL_FILE_EXTENSION"
        return externalFolder.resolve(name)
    }

    private fun writeHeader() {
        header.position(0)
        channel.write(header, 0)
    }

    private fun createExternalStub(): ByteBuffer = ByteBuffer.allocate(CHUNK_HEADER_SIZE)
        .putInt(1)
        .put((version.id or EXTERNAL_STREAM_FLAG).toByte())
        .flip()

    private fun padToFullSector() {
        val size = channel.size().toInt()
        val sectors = sectorCountFromSize(size) * SECTOR_BYTES
        if (size != sectors) {
            val buffer = PADDING_BUFFER.duplicate()
            buffer.position(0)
            channel.write(buffer, (sectors - 1).toLong())
        }
    }

    fun clear(position: ChunkPosition) {
        val offsetIndex = offsetIndex(position)
        val offset = offsets.get(offsetIndex)
        if (offset != CHUNK_NOT_PRESENT) {
            offsets.put(offsetIndex, CHUNK_NOT_PRESENT)
            timestamps.put(offsetIndex, timestamp())
            writeHeader()
            Files.deleteIfExists(resolveExternalChunkPath(position))
            usedSectors.free(sectorNumber(offset), sectorCount(offset))
        }
    }

    override fun close() {
        try {
            padToFullSector()
        } finally {
            channel.use { it.force(true) }
        }
    }

    private inner class ChunkBuffer(private val position: ChunkPosition) : ByteArrayOutputStream(SECTOR_BYTES * 2) {

        init {
            repeat(4) { write(0) }
            write(version.id)
        }

        override fun close() {
            val buffer = ByteBuffer.wrap(buf, 0, count)
            buffer.putInt(0, count - CHUNK_HEADER_SIZE + 1)
            this@RegionFile.write(position, buffer)
        }
    }

    private class RegionBitmap {

        private val used = BitSet()

        fun force(from: Int, to: Int) {
            used.set(from, from + to)
        }

        fun free(from: Int, to: Int) {
            used.clear(from, from + to)
        }

        fun allocate(bits: Int): Int {
            var i = 0
            while (true) {
                val nextClearBit = used.nextClearBit(i)
                val nextSetBit = used.nextSetBit(nextClearBit)
                if (nextSetBit == -1 || nextSetBit - nextClearBit >= bits) {
                    return nextClearBit.apply { force(this, bits) }
                }
                i = nextSetBit
            }
        }
    }

    companion object {

        private val LOGGER = logger<RegionFile>()
        private const val SECTOR_BYTES = 4096
        private const val SECTOR_INTS = 1024
        private const val CHUNK_HEADER_SIZE = 5
        private const val HEADER_OFFSET = 0
        private val PADDING_BUFFER = ByteBuffer.allocateDirect(1)
        private const val EXTERNAL_FILE_EXTENSION = ".mcc"
        private const val EXTERNAL_STREAM_FLAG = 128
        private const val EXTERNAL_CHUNK_THRESHOLD = 256
        private const val CHUNK_NOT_PRESENT = 0

        private val DSYNC_FLAGS = setOf(
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
            StandardOpenOption.DSYNC,
        )
        private val STANDARD_FLAGS = setOf(
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
        )

        @JvmStatic
        private fun timestamp(): Int = (System.currentTimeMillis() / 1000L).toInt()

        @JvmStatic
        private fun offsetIndex(position: ChunkPosition): Int = (position.x and 31) + (position.z and 31) * 32

        @JvmStatic
        private fun sectorNumber(sector: Int): Int = sector shr 8 and 0xFFFFFF

        @JvmStatic
        private fun sectorCount(sector: Int): Int = sector and 255

        @JvmStatic
        private fun sectorCountFromSize(size: Int): Int = (size + SECTOR_BYTES - 1) / SECTOR_BYTES

        @JvmStatic
        private fun isExternalStreamChunk(compressionType: Byte): Boolean = compressionType.toInt() and 0x80 != 0

        @JvmStatic
        private fun externalChunkVersion(compressionType: Byte): Byte = (compressionType.toInt() and 0xFFFFFF7F.toInt()).toByte()

        @JvmStatic
        private fun createStream(
            buffer: ByteBuffer,
            length: Int
        ): ByteArrayInputStream = ByteArrayInputStream(buffer.array(), buffer.position(), length)

        @JvmStatic
        private fun packSectorOffset(sectors: Int, requiredSectors: Int): Int = (sectors shl 8) or requiredSectors
    }
}
