package org.kryptonmc.krypton.world.region

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Manages region files. That's literally it.
 *
 * @author Callum Seabrook
 */
class RegionFileManager(
    private val folder: File,
    private val synchronizeWrites: Boolean
) : AutoCloseable {

    fun read(position: ChunkPosition) = getRegionFile(position).getChunkDataInputStream(position).use {
        if (it == null) return CompoundBinaryTag.empty()
        BinaryTagIO.unlimitedReader().read(it as InputStream) // avoid overload resolution ambiguity by casting
    }

    fun write(position: ChunkPosition, tag: CompoundBinaryTag) =
        getRegionFile(position).getChunkDataOutputStream(position).use {
            BinaryTagIO.writer().write(tag, it as OutputStream) // avoiding overload resolution ambiguity again
        }

    private fun getRegionFile(position: ChunkPosition): RegionFile {
        val serialized = position.regionX combine position.regionZ
        val cachedFile = REGION_CACHE.getAndMoveToFirst(serialized)
        if (cachedFile != null) return cachedFile

        if (REGION_CACHE.size >= 256) REGION_CACHE.removeLast().close()
        folder.mkdirs()

        val file = File(folder, "r.${position.regionX}.${position.regionZ}.mca")
        val regionFile = RegionFile(file.toPath(), synchronizeWrites, folder.toPath(), RegionFileCompression.ZLIB)
        REGION_CACHE.putAndMoveToFirst(serialized, regionFile)
        return regionFile
    }

    override fun close() = REGION_CACHE.values.forEach { it.close() }

    fun flush() = REGION_CACHE.values.forEach { it.flush() }

    companion object {

        private val REGION_CACHE = Long2ObjectLinkedOpenHashMap<RegionFile>()
    }
}

// this & 0xFFFFFFFFL | (other & 0xFFFFFFFFL) << 32
private infix fun Int.combine(other: Int) = (toLong() and 0xFFFFFFFFL) or ((other.toLong() and 0xFFFFFFFFL) shl 32)