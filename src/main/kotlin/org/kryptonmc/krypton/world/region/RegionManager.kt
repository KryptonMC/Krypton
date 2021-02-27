package org.kryptonmc.krypton.world.region

import net.kyori.adventure.nbt.*
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.registry.toNamespacedKey
import org.kryptonmc.krypton.world.Biome
import org.kryptonmc.krypton.world.chunk.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.net.URI
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream
import kotlin.math.floor
import kotlin.math.sqrt

class RegionManager {

    val regions = mutableListOf<Region>()

    init {
        LOGGER.debug("Loading region at 0, 0 for world hardcore")
        val folder = File(URI.create("file://${System.getProperty("krypton.region.dir")}"))
        regions += loadRegion(folder, 0, 0)
        LOGGER.debug("Region loaded!")
    }

    fun loadRegion(folder: File, x: Int, z: Int): Region {
        val file = RandomAccessFile(File(folder, "r.$x.$z.mca"), "r")
        val chunks = mutableListOf<Chunk>()

        for (i in 0 until 1024) {
            file.seek(i * 4L)
            var offset = file.read() shl 16
            offset = offset or ((file.read() and 0xFF) shl 8)
            offset = offset or (file.read() and 0xFF)
            if (file.readByte() == 0.toByte()) continue

            file.seek(4096L + i * 4)
            val timestamp = file.readInt() // ignored for now

            file.seek(4096L * offset + 4)
            chunks += deserializeChunk(file)
        }

        return Region(x, z, chunks)
    }

    private fun deserializeChunk(file: RandomAccessFile): Chunk {
        val compressionType = file.readByte()
        val bufferedStream = decompress(file, compressionType)
        val nbt = BinaryTagIO.unlimitedReader().read(bufferedStream).getCompound("Level")

//        val carvingMasks = nbt.getCompound("CarvingMasks")
        val heightmaps = nbt.getCompound("Heightmaps").map { (key, value) ->
            HeightmapType.valueOf(key) to (value as LongArrayBinaryTag).toList()
        }
//        val lights = nbt.getList("Lights").map { light ->
//            (light as ListBinaryTag).map { (it as ShortBinaryTag).value() }
//        }
//        val liquidsToBeTicked = nbt.getList("LiquidsToBeTicked").map { liquid ->
//            (liquid as ListBinaryTag).map { (it as ShortBinaryTag).value() }
//        }
//        val liquidTicks = nbt.getList("LiquidTicks").map { it as CompoundBinaryTag }
//        val postProcessing = nbt.getList("PostProcessing").map { element ->
//            (element as ListBinaryTag).map { (it as ShortBinaryTag).value() }
//        }

        val sections = nbt.getList("Sections").map { section ->
            val nbtSection = section as CompoundBinaryTag

            val palette = nbtSection.getList("Palette").map { block ->
                (block as CompoundBinaryTag).let { nbtBlock ->
                    ChunkBlock(
                        nbtBlock.getString("Name").toNamespacedKey(),
                        nbtBlock.getCompound("Properties").associate { it.key to it.value }
                    )
                }
            }

            ChunkSection(
                nbtSection.getByte("Y"),
                nbtSection.getByteArray("BlockLight").toList(),
                nbtSection.getByteArray("SkyLight").toList(),
                palette,
                nbtSection.getLongArray("BlockStates").toList()
            )
        }

//        val tileEntities = nbt.getList("TileEntities").map {
//            (it as CompoundBinaryTag).toBlockEntity()
//        }
//        val tileTicks = nbt.getList("TileTicks").map {
//            val nbtTileTick = it as CompoundBinaryTag
//            TileTick(
//                Block(),
//                nbt.getInt("p"),
//                nbt.getInt("t")
//            )
//        }
//        val toBeTicked = nbt.getList("ToBeTicked").map { element ->
//            (element as ListBinaryTag).map { (it as ShortBinaryTag).value() }
//        }

//        val nbtStructures = nbt.getCompound("Structures")
//
//        val structureReferences = nbtStructures.getCompound("References").associate { reference ->
//            reference.key to (reference.value as LongArrayBinaryTag).map {
//                val x = (it shr 32).toInt()
//                val z = it.toInt()
//                ChunkPosition(x, z)
//            }
//        }
//        val structureStarts = nbtStructures.getCompound("Starts").map { start ->
//            (start as CompoundBinaryTag).let {
//                Structure()
//            }
//        }
//
//        val structures = StructureData(
//            structureReferences,
//            structureStarts
//        )

        val chunkData = ChunkData(
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            heightmaps,
            nbt.getLong("LastUpdate"),
            nbt.getLong("InhabitedTime"),
            sections
        )

        return Chunk(ChunkPosition(nbt.getInt("xPos"), nbt.getInt("zPos")), chunkData)
    }

    private fun decompress(file: RandomAccessFile, type: Byte): BufferedInputStream = when (type.toInt()) {
        0 -> BufferedInputStream(FileInputStream(file.fd))
        1 -> BufferedInputStream(GZIPInputStream(FileInputStream(file.fd)))
        2 -> BufferedInputStream(InflaterInputStream(FileInputStream(file.fd)))
        else -> throw UnsupportedOperationException("Unsupported compression type: $type")
    }

    fun chunkInSpiral(id: Int, offset: Int = 0): ChunkPosition {
        if (id == 0) return ChunkPosition(0 + offset, 0 + offset)

        val n = id - 1
        val r = floor((sqrt(n + 1.0) - 1) / 2) + 1
        val p = (8 * r * (r - 1)) / 2
        val en = r * 2
        val a = (1 + n - p) % (r * 8)

        var x = 0.0
        var z = 0.0

        when (floor(a / (r * 2)).toInt()) {
            0 -> {
                x = a - r
                z = -r
            }
            1 -> {
                x = r
                z = (a % en) - r
            }
            2 -> {
                x = r - (a % en)
                z = r
            }
            3 -> {
                x = -r
                z = r - (a % en)
            }
        }

        return ChunkPosition(x.toInt() + offset, z.toInt() + offset)
    }

    companion object {

        private val LOGGER = logger<RegionManager>()
    }
}