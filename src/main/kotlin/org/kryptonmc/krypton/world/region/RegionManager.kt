package org.kryptonmc.krypton.world.region

import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import org.kryptonmc.krypton.config.WorldConfig
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.registry.toNamespacedKey
import org.kryptonmc.krypton.world.Biome
import org.kryptonmc.krypton.world.chunk.*
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.file.Path
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.system.exitProcess

class RegionManager(config: WorldConfig) {

    private val folder = File(Path.of("").toAbsolutePath().toFile(), "${config.name}/region")

    init {
        if (!folder.exists()) {
            LOGGER.error("ERROR! World with name ${config.name} does not exist!")
            exitProcess(0)
        }
    }

    fun loadRegionFromChunk(position: ChunkPosition): Region {
        return loadRegion(folder, floor(position.x / 32.0).toInt(), floor(position.z / 32.0).toInt())
    }

    private fun loadRegion(folder: File, x: Int, z: Int): Region {
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
        val compressionType = when (val type = file.readByte().toInt()) {
            0 -> BinaryTagIO.Compression.NONE
            1 -> BinaryTagIO.Compression.GZIP
            2 -> BinaryTagIO.Compression.ZLIB
            else -> throw UnsupportedOperationException("Unknown compression type $type")
        }
        val nbt = BinaryTagIO.unlimitedReader().read(FileInputStream(file.fd), compressionType).getCompound("Level")

//        val carvingMasks = nbt.getCompound("CarvingMasks")
        val heightmaps = nbt.getCompound("Heightmaps")

        val motionBlocking = LongArrayBinaryTag.of(*heightmaps.getLongArray("MOTION_BLOCKING"))
        val oceanFloor = LongArrayBinaryTag.of(*heightmaps.getLongArray("OCEAN_FLOOR"))
        val worldSurface = LongArrayBinaryTag.of(*heightmaps.getLongArray("WORLD_SURFACE"))
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
                        nbtBlock.getCompound("Properties").associate { it.key to (it.value as StringBinaryTag).value() }
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
            Heightmaps(motionBlocking, oceanFloor, worldSurface),
            nbt.getLong("LastUpdate"),
            nbt.getLong("InhabitedTime"),
            sections
        )

        return Chunk(ChunkPosition(nbt.getInt("xPos"), nbt.getInt("zPos")), chunkData)
    }

    fun chunkInSpiral(id: Int, xOffset: Int = 0, zOffset: Int = 0): ChunkPosition {
        if (id == 0) return ChunkPosition(0 + xOffset, 0 + zOffset)

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

        return ChunkPosition(x.toInt() + xOffset, z.toInt() + zOffset)
    }

    companion object {

        private val LOGGER = logger<RegionManager>()
    }
}