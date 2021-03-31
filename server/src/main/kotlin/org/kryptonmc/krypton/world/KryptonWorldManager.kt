package org.kryptonmc.krypton.world

import com.github.benmanes.caffeine.cache.Caffeine
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.nbt.*
import net.kyori.adventure.nbt.BinaryTagIO.Compression.*
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(override val server: KryptonServer, config: WorldConfig) : WorldManager {

    internal val folder = File(CURRENT_DIRECTORY, config.name)
    private val regionFolder = File(folder, "/region")

    private val chunkCache = Caffeine.newBuilder()
        .maximumSize(512)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<Vector, KryptonChunk>()

    override val worlds = mutableMapOf<String, KryptonWorld>()

    init {
        LOGGER.info("Loading world ${config.name}...")
        if (!folder.exists()) {
            LOGGER.error("ERROR: World with name ${config.name} does not exist!")
            exitProcess(0)
        }

        worlds[config.name] = loadWorld(File(folder, "level.dat"))
        LOGGER.info("World loaded!")
    }

    override fun load(name: String) = loadWorld(File(folder, name))

    private fun loadWorld(file: File): KryptonWorld {
        val nbt = BinaryTagIO.unlimitedReader().read(file.toPath(), GZIP).getCompound("Data")

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
            Difficulty.fromId(nbt.getByte("Difficulty").toInt()),
            nbt.getBoolean("DifficultyLocked"),
            nbt.getCompound("GameRules").associate { Gamerule.fromName(it.key) to (it.value as StringBinaryTag).value() },
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
            nbt.getLong("RandomSeed"),
            mutableListOf(),
            nbt.getList("ServerBrands").add(StringBinaryTag.of("Krypton")).map { (it as StringBinaryTag).value() }.toSet()
        )
    }

    fun loadChunks(positions: List<Vector>): Map<Vector, KryptonChunk> {
        val chunks = mutableMapOf<Vector, KryptonChunk>()
        val groupedByRegion = mutableMapOf<Vector, MutableList<Vector>>()

        positions.forEach {
            if (chunkCache.getIfPresent(it) != null) {
                chunks += it to chunkCache.getIfPresent(it)!!
                return@forEach
            }

            val regionX = floor(it.x / 32.0)
            val regionZ = floor(it.z / 32.0)

            groupedByRegion.getOrPut(Vector(regionX, 0.0, regionZ)) { mutableListOf() } += it
        }

        groupedByRegion.forEach { (key, value) ->
            val region = loadRegion(regionFolder, key.x.toInt(), key.z.toInt(), value)
            chunks += region.chunks
        }

        return chunks
    }

    private fun loadRegion(folder: File, x: Int, z: Int, positions: List<Vector>): Region {
        val file = RandomAccessFile(File(folder, "r.$x.$z.mca"), "r")
        val chunks = mutableMapOf<Vector, KryptonChunk>()

        repeat(1024) { i ->
            file.seek(i * 4L)
            var offset = file.read() shl 16
            offset = offset or ((file.read() and 0xFF) shl 8)
            offset = offset or (file.read() and 0xFF)
            if (file.readByte() == 0.toByte()) return@repeat

            file.seek(4096L + i * 4)
            val timestamp = file.readInt()

            file.seek(4096L * offset + 4)
            deserializeChunk(file, positions)?.let { chunks += it }
        }

        return Region(x, z, chunks)
    }

    private fun deserializeChunk(file: RandomAccessFile, positions: List<Vector>): Pair<Vector, KryptonChunk>? {
        val compressionType = when (val type = file.readByte().toInt()) {
            0 -> NONE
            1 -> GZIP
            2 -> ZLIB
            else -> throw UnsupportedOperationException("Unknown compression type $type")
        }
        val nbt = BinaryTagIO.unlimitedReader().read(FileInputStream(file.fd), compressionType).getCompound("Level")

        val location = Vector(nbt.getInt("xPos"), 0, nbt.getInt("zPos"))
        if (location !in positions) return null

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
                nbtSection.getByteArray("BlockLight"),
                nbtSection.getByteArray("SkyLight"),
                palette,
                nbtSection.getLongArray("BlockStates")
            )
        }

        return location to KryptonChunk(
            location,
            sections,
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            nbt.getLong("LastUpdate"),
            nbt.getLong("inhabitedTime"),
            Heightmaps(motionBlocking, oceanFloor, worldSurface)
        ).apply { chunkCache.put(location, this) }
    }

    fun save(world: KryptonWorld) {
        val dataFile = File(folder, "level.dat")

        val customBossEvents = world.bossbars.associate { bossbar ->
            val players = bossbar.players.map {
                CompoundBinaryTag.builder()
                    .putLong("L", it.leastSignificantBits)
                    .putLong("M", it.mostSignificantBits)
                    .build()
            }

            val createWorldFog = BossBar.Flag.CREATE_WORLD_FOG in bossbar.flags
            val darkenScreen = BossBar.Flag.DARKEN_SCREEN in bossbar.flags
            val playBossMusic = BossBar.Flag.PLAY_BOSS_MUSIC in bossbar.flags

            bossbar.id.toString() to CompoundBinaryTag.builder()
                .put("Players", ListBinaryTag.of(BinaryTagTypes.COMPOUND, players))
                .putString("Color", bossbar.color.name)
                .putBoolean("CreateWorldFog", createWorldFog)
                .putBoolean("DarkenScreen", darkenScreen)
                .putInt("Max", 20)
                .putInt("Value", 20)
                .putString("Name", GsonComponentSerializer.gson().serialize(bossbar.name))
                .putString("Overlay", bossbar.overlay.name)
                .putBoolean("PlayBossMusic", playBossMusic)
                .putBoolean("Visible", bossbar.visible)
                .build()
        }

        val gamerules = world.gamerules.transform { (rule, value) -> rule.rule to StringBinaryTag.of(value) }
        val dimensions = world.generationSettings.dimensions.transform { (key, value) -> key.toString() to value.toNBT() }

        BinaryTagIO.writer().write(CompoundBinaryTag.builder().put("Data", CompoundBinaryTag.builder()
            .putBoolean("allowCommands", false)
            .putDouble("BorderCenterX", world.border.center.x)
            .putDouble("BorderCenterZ", world.border.center.z)
            .putDouble("BorderDamagePerBlock", world.border.damageMultiplier)
            .putDouble("BorderSize", world.border.size)
            .putDouble("BorderSafeZone", world.border.safeZone)
            .putDouble("BorderSizeLerpTarget", world.border.sizeLerpTarget)
            .putLong("BorderSizeLerpTime", world.border.sizeLerpTime)
            .putDouble("BorderWarningBlocks", world.border.warningBlocks)
            .putDouble("BorderWarningTime", world.border.warningTime)
            .putInt("clearWeatherTime", world.clearWeatherTime)
            .put("CustomBossEvents", CompoundBinaryTag.from(customBossEvents))
            .put("DataPacks", CompoundBinaryTag.builder()
                .put("Enabled", ListBinaryTag.of(BinaryTagTypes.STRING, listOf(StringBinaryTag.of("vanilla"))))
                .put("Disabled", ListBinaryTag.empty())
                .build())
            .putInt("DataVersion", LevelDataVersion.ID)
            .putLong("DayTime", world.dayTime)
            .putByte("Difficulty", world.difficulty.ordinal.toByte())
            .putBoolean("DifficultyLocked", world.difficultyLocked)
            .put("GameRules", CompoundBinaryTag.from(gamerules))
            .put("WorldGenSettings", CompoundBinaryTag.builder()
                .putLong("seed", world.generationSettings.seed)
                .putBoolean("generate_features", world.generationSettings.generateStructures)
                .put("dimensions", CompoundBinaryTag.from(dimensions))
                .build())
            .putInt("GameType", world.gamemode.ordinal)
            .putBoolean("hardcore", world.isHardcore)
            .putBoolean("initialized", true)
            .putString("Krypton.Version", KryptonServerInfo.version)
            .putLong("LastPlayed", world.lastPlayed.toInstant(ZoneOffset.UTC).toEpochMilli())
            .putString("LevelName", world.name)
            .putBoolean("MapFeatures", world.mapFeatures)
            .putBoolean("raining", world.isRaining)
            .putInt("rainTime", world.rainTime)
            .putLong("RandomSeed", world.randomSeed)
            .put("ServerBrands", ListBinaryTag.from(world.serverBrands.map { StringBinaryTag.of(it) }))
            .putInt("SpawnX", world.spawnLocation.x.toInt())
            .putInt("SpawnY", world.spawnLocation.y.toInt())
            .putInt("SpawnZ", world.spawnLocation.z.toInt())
            .putBoolean("thundering", world.isThundering)
            .putLong("Time", world.time)
            .putInt("version", NBT_VERSION)
            .put("Version", CompoundBinaryTag.builder()
                .putInt("Id", LevelDataVersion.ID)
                .putString("Name", LevelDataVersion.NAME)
                .putBoolean("Snapshot", LevelDataVersion.SNAPSHOT)
                .build())
            .putInt("WanderingTraderSpawnChance", 25)
            .putInt("WanderingTraderSpawnDelay", 24000)
            .build()).build(), dataFile.toPath(), GZIP)
    }

    /**
     * Calculates a chunk position from a given [id] in a spiral pattern.
     *
     * **Algorithm:**
     *
     * Given n, an index in the squared spiral
     * p, the sum of a point in the inner square
     * and a, the position on the current square
     *
     * n = p + a
     *
     * Credit for this algorithm goes to
     * [davidonet](https://stackoverflow.com/users/1068670/davidonet) (for the original algorithm),
     * and [Esophose](https://github.com/Esophose) (for the Kotlin conversion and modifications)
     *
     * See [here](https://stackoverflow.com/questions/398299/looping-in-a-spiral) for original
     *
     * @param id the id in the spiral
     * @param xOffset an optional X offset
     * @param zOffset an optional Z offset
     * @return a [Vector] containing the calculated position in the spiral.
     * Note: as chunk positions are two-dimensional, the Y value of this will always be 0.0
     */
    fun chunkInSpiral(id: Int, xOffset: Double = 0.0, zOffset: Double = 0.0): Vector {
        // if the id is 0 then we know we're in the centre
        if (id == 0) return Vector(0.0 + xOffset, 0.0, 0.0 + zOffset)

        val index = id - 1

        // compute radius (inverse arithmetic sum of 8 + 16 + 24 + ...)
        val radius = floor((sqrt(index + 1.0) - 1) / 2) + 1

        // compute total point on radius -1 (arithmetic sum of 8 + 16 + 24 + ...)
        val p = (8 * radius * (radius - 1)) / 2

        // points by face
        val en = radius * 2

        // compute de position and shift it so the first is (-r, -r) but (-r + 1, -r)
        // so the square can connect
        val a = (1 + index - p) % (radius * 8)

        return when (floor(a / (radius * 2)).toInt()) {
            // find the face (0 = top, 1 = right, 2 = bottom, 3 = left)
            0 -> Vector((a - radius) + xOffset, 0.0, -radius + zOffset)
            1 -> Vector(radius + xOffset, 0.0, ((a % en) - radius) + zOffset)
            2 -> Vector((radius - (a % en)) + xOffset, 0.0, radius + zOffset)
            3 -> Vector(-radius + xOffset, 0.0, (radius - (a % en)) + zOffset)
            else -> Vector.ZERO
        }
    }

    companion object {

        private const val DEFAULT_BUILD_LIMIT = 256

        private val LOGGER = logger<KryptonWorldManager>()
    }
}

// I know this looks stupid, and it probably is stupid, but this avoids me having to use toByte()
// also means I don't declare extra unnecessary integers on the stack
private const val ONE: Byte = 1

val ByteBinaryTag.boolean: Boolean
    get() {
        if (value() > ONE) throw IllegalArgumentException("Boolean value cannot be greater than 1!")
        return value() == ONE
    }

fun <K, V, K1, V1> Map<K, V>.transform(function: (Map.Entry<K, V>) -> Pair<K1, V1>): Map<K1, V1> {
    val temp = mutableMapOf<K1, V1>()
    for (entry in this) {
        temp += function(entry)
    }
    return temp
}