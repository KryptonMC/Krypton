package org.kryptonmc.krypton.world.region

import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.openChannel
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

    private val file: FileChannel
    private val header = ByteBuffer.allocateDirect(8192)
    private val offsets: IntBuffer
    private val timestamps: IntBuffer
    private val usedSectors = RegionBitmap()

    init {
        require(externalDirectory.isDirectory()) { "Expected directory, got ${externalDirectory.toAbsolutePath()}" }
        offsets = header.asIntBuffer().limit(1024)
        header.position(4096)
        timestamps = header.asIntBuffer()
        file = path.openChannel(if (synchronizeWrites) SHARED_FLAGS + StandardOpenOption.DSYNC else SHARED_FLAGS)
        usedSectors.force(0, 2)
        header.position(0)

        val first = file.read(header, 0L)
        if (first != -1) {
            if (first != 8192) LOGGER.warn("Region file $path has truncated header: $first")
            for (i in 0 until 1024) {
                val offset = offsets[i]
                if (offset == 0) continue

                val sectorNumber = offset.sectorNumber
                val sectorCount = offset.sectorCount
                if (sectorNumber < 2) {
                    LOGGER.warn("Region file $path has an invalid sector at index $i. Sector $sectorNumber overlaps with header")
                    offsets[i] = 0
                    continue
                }
                if (sectorCount == 0) {
                    LOGGER.warn("Region file $path has an invalid sector at index $i. Size has to be > 0")
                    offsets[i] = 0
                    continue
                }
                if (sectorNumber * 4096L > path.fileSize()) {
                    LOGGER.warn("Region file $path has an invalid sector at index $i. Sector $sectorNumber is out of bounds")
                    offsets[i] = 0
                    continue
                }
                usedSectors.force(sectorNumber, sectorCount)
            }
        }
    }

    /**
     * Gets a [DataInputStream] containing all of the chunk data for a chunk at the specified [position]
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
        file.read(buffer, sectorNumber * 4096L)
        buffer.flip()

        if (buffer.remaining() < 5) {
            LOGGER.error("Chunk $position's header is truncated! Expected $size but got ${buffer.remaining()}!")
            return null
        }

        val length = buffer.int
        val compressionType = buffer.get()
        if (length == 0) {
            LOGGER.warn("Chunk $position is allocated, but stream is missing.")
            return null
        }

        val dataLength = length - 1
        if (compressionType.isExternalStreamChunk) {
            if (dataLength != 0) LOGGER.warn("Chunk has both internal and external streams.")
            return createExternalChunkInputStream(position, compressionType.externalChunkVersion)
        }
        if (dataLength > buffer.remaining()) {
            LOGGER.error("Chunk $position's stream is truncated! Expected $dataLength but got ${buffer.remaining()}")
            return null
        }
        if (dataLength < 0) {
            LOGGER.error("Declared size $length of chunk $position is negative!")
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
    fun getChunkDataOutputStream(position: ChunkPosition) =
        DataOutputStream(BufferedOutputStream(compression.compress(ChunkBuffer(position))))

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
            LOGGER.warn("Saving oversized chunk $position ($length bytes) to external file $path.")
            requiredSectors = 1
            sectors = usedSectors.allocate(length)
            action = writeExternal(path, buffer)
            val externalStub = createExternalStub()
            file.write(externalStub, sectors * 4096L)
        } else {
            sectors = usedSectors.allocate(requiredSectors)
            action = { externalDirectory.resolve("c.${position.x}.${position.z}.mcc").deleteIfExists() }
            file.write(buffer, sectors * 4096L)
        }
        val timestamp = (Instant.now().toEpochMilli() / 1000).toInt()
        offsets[offsetIndex] = (sectors shl 8) or requiredSectors
        timestamps[offsetIndex] = timestamp
        writeHeader()
        action()
        if (sectorNumber != 0) usedSectors.free(sectorNumber, sectorCount)
    }

    private fun createExternalChunkInputStream(position: ChunkPosition, compressionType: Byte): DataInputStream? {
        val path = externalDirectory.resolve("c.${position.x}.${position.z}.mcc")
        if (!path.isRegularFile()) {
            LOGGER.error("External chunk path $path is not a file!")
            return null
        }
        return createChunkInputStream(position, compressionType, path.inputStream())
    }

    private fun createChunkInputStream(position: ChunkPosition, compressionType: Byte, input: InputStream): DataInputStream? {
        val compression = RegionFileCompression.fromId(compressionType)
        if (compression == null) {
            LOGGER.error("Chunk $position has an invalid compression type! Type: $compressionType")
            return null
        }
        return DataInputStream(BufferedInputStream(compression.decompress(input)))
    }

    private fun writeExternal(path: Path, buffer: ByteBuffer): () -> Unit {
        val temp = externalDirectory.createTempFile("tmp")
        path.openChannel(StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            buffer.position(5)
            it.write(buffer)
        }
        return { temp.moveTo(path, StandardCopyOption.REPLACE_EXISTING) }
    }

    private fun writeHeader() {
        header.position(0)
        file.write(header, 0)
    }

    private fun createExternalStub() =
        ByteBuffer.allocate(5).putInt(1).put((compression.ordinal or 0x80).toByte()).flip()

    private fun padToFullSector() {
        val size = file.size()
        val sectors = size + 4096 - 1
        if (size != sectors) {
            val buffer = PADDING_BUFFER.duplicate()
            buffer.position(0)
            file.write(buffer, sectors - 1)
        }
    }

    fun flush() = file.force(true)

    override fun close() = try {
        padToFullSector()
    } finally {
        file.use { it.force(true) }
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

    companion object {

        private val SHARED_FLAGS = setOf(
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
        )
        private val PADDING_BUFFER = ByteBuffer.allocateDirect(1)

        private val LOGGER = logger<RegionFile>()

        private val Int.sectorNumber: Int
            get() = shr(8) and 0xFFFFFF

        private val Int.sectorCount: Int
            get() = and(0xFF)

        private val ChunkPosition.offsetIndex: Int
            get() = regionLocalX + regionLocalZ * 32

        private val Byte.isExternalStreamChunk: Boolean
            get() = (toInt() and 0x80) != 0

        private val Byte.externalChunkVersion: Byte
            get() = (toInt() and 0xFFFFFF7F.toInt()).toByte()
    }
}

class RegionBitmap {

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

operator fun IntBuffer.set(index: Int, value: Int): IntBuffer = put(index, value)
