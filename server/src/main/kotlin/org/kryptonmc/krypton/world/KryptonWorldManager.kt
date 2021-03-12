package org.kryptonmc.krypton.world

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.*
import org.kryptonmc.krypton.config.WorldConfig
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.world.chunk.*
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.generation.toGenerator
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.file.Path
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(override val server: KryptonServer, config: WorldConfig) : WorldManager {

    private val folder = File(Path.of("").toAbsolutePath().toFile(), config.name)
    private val regionFolder = File(folder, "/region")

    override val worlds = mutableMapOf<String, KryptonWorld>()

    init {
        LOGGER.debug("Loading world ${config.name}...")
        if (!folder.exists()) {
            LOGGER.error("ERROR: World with name ${config.name} does not exist!")
            exitProcess(0)
        }

        worlds[config.name] = loadWorld(File(folder, "level.dat"))
        LOGGER.debug("World loaded!")
    }

    override fun load(name: String) = loadWorld(File(folder, name))

    private fun loadWorld(file: File): KryptonWorld {
        val nbt = BinaryTagIO.unlimitedReader().read(file.toPath(), BinaryTagIO.Compression.GZIP).getCompound("Data")

        val bossbars = nbt.getCompound("CustomBossEvents").map { (key, bossbar) ->
            val nbtBossbar = bossbar as CompoundBinaryTag
            val players = nbtBossbar.getList("Players").map {
                val nbtPlayerUUID = it as CompoundBinaryTag
                UUID(nbtPlayerUUID.getLong("M"), nbtPlayerUUID.getLong("L"))
            }

            val flags = mutableSetOf<BossBar.Flag>()
            if (nbtBossbar.getBoolean("CreateWorldFog")) flags += BossBar.Flag.CREATE_WORLD_FOG
            if (nbtBossbar.getBoolean("DarkenScreen")) flags += BossBar.Flag.DARKEN_SCREEN
            if (nbtBossbar.getBoolean("PlayBossMusic")) flags += BossBar.Flag.PLAY_BOSS_MUSIC

            org.kryptonmc.krypton.world.bossbar.Bossbar(
                key.toNamespacedKey(),
                GsonComponentSerializer.gson().deserialize(nbtBossbar.getString("Name")),
                BossBar.Color.NAMES.value(nbtBossbar.getString("Color")) ?: BossBar.Color.WHITE,
                BossBar.Overlay.NAMES.value(nbtBossbar.getString("Overlay")) ?: BossBar.Overlay.PROGRESS,
                nbtBossbar.getInt("Value").toFloat() / nbtBossbar.getInt("Max").toFloat(),
                flags,
                nbtBossbar.getBoolean("Visible"),
                players
            )
        }

        val worldGenSettings = nbt.getCompound("WorldGenSettings").let { settings ->
            val dimensions = settings.getCompound("dimensions").associate { (key, value) ->
                key.toNamespacedKey() to (value as CompoundBinaryTag).let {
                    Dimension(it.getString("type").toNamespacedKey(), it.getCompound("generator").toGenerator())
                }
            }

            WorldGenerationSettings(
                settings.getLong("seed"),
                settings.getBoolean("generate_features"),
                dimensions
            )
        }

        val spawnX = nbt.getInt("SpawnX")
        val spawnZ = nbt.getInt("SpawnZ")

        val spawnLocation = LocationBuilder(
            spawnX.toDouble(),
            nbt.getInt("SpawnY").toDouble(),
            spawnZ.toDouble()
        )

        return KryptonWorld(
            nbt.getString("LevelName"),
            mutableSetOf(),
            bossbars,
            nbt.getBoolean("allowCommands"),
            BorderBuilder(
                nbt.getDouble("BorderCenterX"),
                nbt.getDouble("BorderCenterZ"),
                nbt.getDouble("BorderDamagePerBlock"),
                nbt.getDouble("BorderSize"),
                nbt.getDouble("BorderSafeZone"),
                nbt.getDouble("BorderSizeLerpTarget"),
                nbt.getLong("BorderSizeLerpTime"),
                nbt.getDouble("BorderWarningBlocks"),
                nbt.getDouble("BorderWarningTime")
            ),
            nbt.getInt("clearWeatherTime"),
            nbt.getLong("DayTime"),
            Difficulty.fromId(nbt.getByte("Difficulty").toInt())!!,
            nbt.getBoolean("DifficultyLocked"),
            worldGenSettings,
            requireNotNull(Gamemode.fromId(nbt.getInt("GameType"))),
            nbt.getBoolean("hardcore"),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(nbt.getLong("LastPlayed")), ZoneOffset.UTC),
            nbt.getBoolean("raining"),
            nbt.getBoolean("MapFeatures"),
            nbt.getInt("rainTime"),
            nbt.getLong("RandomSeed"),
            spawnLocation,
            nbt.getBoolean("thundering"),
            nbt.getInt("thunderTime"),
            nbt.getLong("Time"),
            nbt.getInt("version"),
            nbt.getCompound("Version").let {
                WorldVersion(it.getInt("Id"), it.getString("Name"), it.getBoolean("Snapshot"))
            },
            DEFAULT_BUILD_LIMIT,
            nbt.getLong("RandomSeed")
        )
    }

    fun loadRegionFromChunk(position: Vector): Region {
        return loadRegion(regionFolder, floor(position.x / 32.0).toInt(), floor(position.z / 32.0).toInt())
    }

    private fun loadRegion(folder: File, x: Int, z: Int): Region {
        val file = RandomAccessFile(File(folder, "r.$x.$z.mca"), "r")
        val chunks = mutableMapOf<Vector, KryptonChunk>()

        for (i in 0 until 1024) {
            file.seek(i * 4L)
            var offset = file.read() shl 16
            offset = offset or ((file.read() and 0xFF) shl 8)
            offset = offset or (file.read() and 0xFF)
            if (file.readByte() == 0.toByte()) continue

            file.seek(4096L + i * 4)
            val timestamp = file.readInt()

            file.seek(4096L * offset + 4)
            chunks += deserializeChunk(file)
        }

        return Region(x, z, chunks)
    }

    private fun deserializeChunk(file: RandomAccessFile): Pair<Vector, KryptonChunk> {
        val compressionType = when (val type = file.readByte().toInt()) {
            0 -> BinaryTagIO.Compression.NONE
            1 -> BinaryTagIO.Compression.GZIP
            2 -> BinaryTagIO.Compression.ZLIB
            else -> throw UnsupportedOperationException("Unknown compression type $type")
        }
        val nbt = BinaryTagIO.unlimitedReader().read(FileInputStream(file.fd), compressionType).getCompound("Level")

        val heightmaps = nbt.getCompound("Heightmaps")

        val motionBlocking = LongArrayBinaryTag.of(*heightmaps.getLongArray("MOTION_BLOCKING"))
        val oceanFloor = LongArrayBinaryTag.of(*heightmaps.getLongArray("OCEAN_FLOOR"))
        val worldSurface = LongArrayBinaryTag.of(*heightmaps.getLongArray("WORLD_SURFACE"))

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

            KryptonChunkSection(
                nbtSection.getByte("Y").toInt(),
                nbtSection.getByteArray("BlockLight").toList(),
                nbtSection.getByteArray("SkyLight").toList(),
                palette,
                nbtSection.getLongArray("BlockStates").toList()
            )
        }

        val location = Vector(nbt.getInt("xPos").toDouble(), 0.0, nbt.getInt("zPos").toDouble())
        return location to KryptonChunk(
            location,
            sections,
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            nbt.getLong("LastUpdate"),
            nbt.getLong("inhabitedTime"),
            Heightmaps(motionBlocking, oceanFloor, worldSurface)
        )
    }

    fun chunkInSpiral(id: Int, xOffset: Double = 0.0, zOffset: Double = 0.0): Vector {
        if (id == 0) return Vector(0.0 + xOffset, 0.0, 0.0 + zOffset)

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

        return Vector(x + xOffset, 0.0, z + zOffset)
    }

    companion object {

        private const val DEFAULT_BUILD_LIMIT = 256

        private val LOGGER = logger<KryptonWorldManager>()
    }
}