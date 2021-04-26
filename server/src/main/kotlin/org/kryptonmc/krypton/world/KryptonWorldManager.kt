package org.kryptonmc.krypton.world

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.BinaryTagIO.Compression.GZIP
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.world.Biome
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.WorldManager
import org.kryptonmc.krypton.api.world.WorldVersion
import org.kryptonmc.krypton.util.calculateBits
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.Heightmap.Type.MOTION_BLOCKING
import org.kryptonmc.krypton.world.Heightmap.Type.OCEAN_FLOOR
import org.kryptonmc.krypton.world.Heightmap.Type.WORLD_SURFACE
import org.kryptonmc.krypton.world.bossbar.Bossbar
import org.kryptonmc.krypton.world.chunk.ChunkBlock
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkSection
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.data.BitStorage
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.generation.toGenerator
import org.kryptonmc.krypton.world.region.RegionFileManager
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.LinkedList
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(override val server: KryptonServer, name: String, synchronizeWrites: Boolean) : WorldManager {

    internal val folder = File(CURRENT_DIRECTORY, name)
    private val regionFolder = File(folder, "/region")

    private val regionFileManager = RegionFileManager(regionFolder, synchronizeWrites)

    private val chunkCache: Cache<ChunkPosition, KryptonChunk> = Caffeine.newBuilder()
        .maximumSize(512)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build()

    override val worlds = mutableMapOf<String, KryptonWorld>()

    override val default: KryptonWorld

    init {
        LOGGER.info("Loading world $name...")
        if (!folder.exists()) {
            LOGGER.error("ERROR: World with name $name does not exist!")
            exitProcess(0)
        }

        default = loadWorld(folder)
        worlds[name] = default
        LOGGER.info("World loaded!")
    }

    override fun load(name: String): KryptonWorld {
        val folder = File(CURRENT_DIRECTORY, name)
        if (!folder.exists()) {
            LOGGER.error("ERROR: World with name $name does not exist!")
            throw IllegalArgumentException("World with name $name does not exist!")
        }
        return loadWorld(folder)
    }

    override fun save(world: World) {
        // guard against passing in world objects that don't extend KryptonWorld
        if (world !is KryptonWorld) {
            throw IllegalArgumentException("Cannot save custom world objects! Expected type of KryptonWorld, got ${world::class.java.name}")
        }

        world.save()
        if (world.chunks.isEmpty()) return

        val lastUpdate = world.time
        world.chunks.forEach {
            chunkCache.invalidate(it.position)
            it.lastUpdate = lastUpdate
            regionFileManager.write(it.position, it.serialize())
        }
    }

    override fun contains(name: String) = worlds.containsKey(name)

    // TODO: Possibly also make this preload spawn chunks
    private fun loadWorld(folder: File): KryptonWorld {
        val dataFile = File(folder, "level.dat")
        val nbt = BinaryTagIO.unlimitedReader().read(dataFile.toPath(), GZIP).getCompound("Data")

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

            Bossbar(
                BossBar.bossBar(
                    GsonComponentSerializer.gson().deserialize(nbtBossbar.getString("Name")),
                    nbtBossbar.getInt("Value").toFloat() / nbtBossbar.getInt("Max").toFloat(),
                    BossBar.Color.NAMES.value(nbtBossbar.getString("Color")) ?: BossBar.Color.WHITE,
                    BossBar.Overlay.NAMES.value(nbtBossbar.getString("Overlay")) ?: BossBar.Overlay.PROGRESS,
                    flags
                ),
                key.toNamespacedKey(),
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
            loadUUID(folder),
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

    private fun loadUUID(folder: File): UUID {
        val uuidFile = File(folder, "uid.dat")
        return if (uuidFile.exists()) {
            DataInputStream(uuidFile.inputStream()).use {
                UUID(it.readLong(), it.readLong())
            }
        } else {
            val uuid = UUID.randomUUID()
            DataOutputStream(uuidFile.outputStream()).use {
                it.writeLong(uuid.mostSignificantBits)
                it.writeLong(uuid.leastSignificantBits)
            }
            return uuid
        }
    }

    fun loadChunks(world: KryptonWorld, positions: List<ChunkPosition>): List<KryptonChunk> {
        val chunks = positions.map { deserializeChunk(world, it) }
        world.chunks += chunks
        return chunks
    }

    private fun deserializeChunk(world: KryptonWorld, position: ChunkPosition): KryptonChunk {
        val cachedChunk = chunkCache.getIfPresent(position)
        if (cachedChunk != null) return cachedChunk

        val nbt = regionFileManager.read(position).getCompound("Level")

        val heightmaps = nbt.getCompound("Heightmaps")

        val sections = nbt.getList("Sections").map { section ->
            val nbtSection = section as CompoundBinaryTag

            val palette = LinkedList(nbtSection.getList("Palette").map { block ->
                (block as CompoundBinaryTag).let { nbtBlock ->
                    val name = nbtBlock.getString("Name").toNamespacedKey()
                    if (name == ChunkBlock.AIR.name) return@let ChunkBlock.AIR
                    ChunkBlock(
                        name,
                        nbtBlock.getCompound("Properties").associate { it.key to (it.value as StringBinaryTag).value() }
                    )
                }
            })

            ChunkSection(
                nbtSection.getByte("Y").toInt(),
                nbtSection.getByteArray("BlockLight"),
                nbtSection.getByteArray("SkyLight"),
                palette,
                BitStorage(palette.size.calculateBits(), 4096, nbtSection.getLongArray("BlockStates"))
            )
        } as MutableList<ChunkSection>

        val carvingMasks = nbt.getCompound("CarvingMasks").let {
            it.getByteArray("AIR") to it.getByteArray("LIQUID")
        }

        return KryptonChunk(
            world,
            position,
            sections,
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            nbt.getLong("LastUpdate"),
            nbt.getLong("inhabitedTime"),
            listOf(
                HeightmapBuilder(LongArrayBinaryTag.of(*heightmaps.getLongArray("MOTION_BLOCKING")), MOTION_BLOCKING),
                HeightmapBuilder(LongArrayBinaryTag.of(*heightmaps.getLongArray("OCEAN_FLOOR")), OCEAN_FLOOR),
                HeightmapBuilder(LongArrayBinaryTag.of(*heightmaps.getLongArray("WORLD_SURFACE")), WORLD_SURFACE)
            ),
            carvingMasks,
            nbt.getCompound("Structures")
        ).apply { chunkCache.put(position, this) }
    }

    fun KryptonWorld.save() {
        val dataFile = File(folder, "level.dat")

        val customBossEvents = bossbars.associate { bossbar ->
            val players = bossbar.players.map {
                CompoundBinaryTag.builder()
                    .putLong("L", it.leastSignificantBits)
                    .putLong("M", it.mostSignificantBits)
                    .build()
            }

            val createWorldFog = BossBar.Flag.CREATE_WORLD_FOG in bossbar.flags()
            val darkenScreen = BossBar.Flag.DARKEN_SCREEN in bossbar.flags()
            val playBossMusic = BossBar.Flag.PLAY_BOSS_MUSIC in bossbar.flags()

            bossbar.id.toString() to CompoundBinaryTag.builder()
                .put("Players", ListBinaryTag.of(BinaryTagTypes.COMPOUND, players))
                .putString("Color", bossbar.color().name)
                .putBoolean("CreateWorldFog", createWorldFog)
                .putBoolean("DarkenScreen", darkenScreen)
                .putInt("Max", 20)
                .putInt("Value", 20)
                .putString("Name", GsonComponentSerializer.gson().serialize(bossbar.name()))
                .putString("Overlay", bossbar.overlay().name)
                .putBoolean("PlayBossMusic", playBossMusic)
                .putBoolean("Visible", bossbar.visible)
                .build()
        }

        val gamerules = gamerules.transform { (rule, value) -> rule.rule to StringBinaryTag.of(value) }
        val dimensions = generationSettings.dimensions.transform { (key, value) -> key.toString() to value.toNBT() }

        BinaryTagIO.writer().write(CompoundBinaryTag.builder().put("Data", CompoundBinaryTag.builder()
            .putBoolean("allowCommands", false)
            .putDouble("BorderCenterX", border.center.x)
            .putDouble("BorderCenterZ", border.center.z)
            .putDouble("BorderDamagePerBlock", border.damageMultiplier)
            .putDouble("BorderSize", border.size)
            .putDouble("BorderSafeZone", border.safeZone)
            .putDouble("BorderSizeLerpTarget", border.sizeLerpTarget)
            .putLong("BorderSizeLerpTime", border.sizeLerpTime)
            .putDouble("BorderWarningBlocks", border.warningBlocks)
            .putDouble("BorderWarningTime", border.warningTime)
            .putInt("clearWeatherTime", clearWeatherTime)
            .put("CustomBossEvents", CompoundBinaryTag.from(customBossEvents))
            .put("DataPacks", CompoundBinaryTag.builder()
                .put("Enabled", ListBinaryTag.of(BinaryTagTypes.STRING, listOf(StringBinaryTag.of("vanilla"))))
                .put("Disabled", ListBinaryTag.empty())
                .build())
            .putInt("DataVersion", LevelDataVersion.ID)
            .putLong("DayTime", dayTime)
            .putByte("Difficulty", difficulty.ordinal.toByte())
            .putBoolean("DifficultyLocked", difficultyLocked)
            .put("GameRules", CompoundBinaryTag.from(gamerules))
            .put("WorldGenSettings", CompoundBinaryTag.builder()
                .putLong("seed", generationSettings.seed)
                .putBoolean("generate_features", generationSettings.generateStructures)
                .put("dimensions", CompoundBinaryTag.from(dimensions))
                .build())
            .putInt("GameType", gamemode.ordinal)
            .putBoolean("hardcore", isHardcore)
            .putBoolean("initialized", true)
            .putString("Krypton.Version", KryptonServerInfo.version)
            .putLong("LastPlayed", lastPlayed.toInstant(ZoneOffset.UTC).toEpochMilli())
            .putString("LevelName", name)
            .putBoolean("MapFeatures", mapFeatures)
            .putBoolean("raining", isRaining)
            .putInt("rainTime", rainTime)
            .putLong("RandomSeed", randomSeed)
            .put("ServerBrands", ListBinaryTag.from(serverBrands.map { StringBinaryTag.of(it) }))
            .putInt("SpawnX", spawnLocation.x.toInt())
            .putInt("SpawnY", spawnLocation.y.toInt())
            .putInt("SpawnZ", spawnLocation.z.toInt())
            .putBoolean("thundering", isThundering)
            .putLong("Time", time)
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

    private fun KryptonChunk.serialize(): CompoundBinaryTag {
        val sections = sections.map { section ->
            val palette = section.palette.map { entry ->
                val properties = CompoundBinaryTag.builder().apply {
                    entry.properties.forEach { putString(it.key, it.value) }
                }.build()

                CompoundBinaryTag.builder()
                    .putString("Name", entry.name.toString())
                    .put("Properties", properties)
                    .build()
            }

            CompoundBinaryTag.builder()
                .putByte("Y", section.y.toByte())
                .apply {
                    if (section.blockLight.isNotEmpty()) putByteArray("BlockLight", section.blockLight)
                    if (section.skyLight.isNotEmpty()) putByteArray("SkyLight", section.skyLight)
                    if (section.blockStates.isNotEmpty()) putLongArray("BlockStates", section.blockStates.data)
                    if (palette.isNotEmpty()) put("Palette", ListBinaryTag.of(BinaryTagTypes.COMPOUND, palette))
                }
                .build()
        }

        return CompoundBinaryTag.builder()
            .putInt("DataVersion", CHUNK_DATA_VERSION)
            .put("Level", CompoundBinaryTag.builder()
                .putIntArray("Biomes", biomes.map { it.id }.toIntArray())
                .put("CarvingMasks", CompoundBinaryTag.builder()
                    .putByteArray("AIR", carvingMasks.first)
                    .putByteArray("LIQUID", carvingMasks.second)
                    .build())
                .put("Heightmaps", CompoundBinaryTag.builder()
                    .putLongArray("MOTION_BLOCKING", heightmaps.getValue(MOTION_BLOCKING).data.data)
                    .putLongArray("MOTION_BLOCKING_NO_LEAVES", LongArray(0))
                    .putLongArray("OCEAN_FLOOR", heightmaps.getValue(OCEAN_FLOOR).data.data)
                    .putLongArray("OCEAN_FLOOR_WG", LongArray(0))
                    .putLongArray("WORLD_SURFACE", heightmaps.getValue(WORLD_SURFACE).data.data)
                    .putLongArray("WORLD_SURFACE_WG", LongArray(0))
                    .build())
                .putLong("LastUpdate", lastUpdate)
                .put("Lights", ListBinaryTag.empty())
                .put("LiquidsToBeTicked", ListBinaryTag.empty())
                .put("LiquidTicks", ListBinaryTag.empty())
                .putLong("InhabitedTime", inhabitedTime)
                .put("PostProcessing", ListBinaryTag.empty())
                .put("Sections", ListBinaryTag.of(BinaryTagTypes.COMPOUND, sections))
                .putString("Status", "full")
                .put("TileEntities", ListBinaryTag.empty())
                .put("TileTicks", ListBinaryTag.empty())
                .put("ToBeTicked", ListBinaryTag.empty())
                .put("Structures", structures)
                .putInt("xPos", position.x)
                .putInt("zPos", position.z)
                .build())
            .build()
    }

    fun saveAll(isAutosave: Boolean = false) {
        worlds.forEach { (_, world) -> save(world) }
        LOGGER.info(if (isAutosave) "Autosave finished" else "Save finished")
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
     * @return a [ChunkPosition] containing the calculated position in the spiral.
     */
    fun chunkInSpiral(id: Int, xOffset: Int = 0, zOffset: Int = 0): ChunkPosition {
        // if the id is 0 then we know we're in the centre
        if (id == 0) return ChunkPosition(0 + xOffset, 0 + zOffset)

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
            0 -> ChunkPosition((a - radius).toInt() + xOffset, -radius.toInt() + zOffset)
            1 -> ChunkPosition(radius.toInt() + xOffset, ((a % en) - radius).toInt() + zOffset)
            2 -> ChunkPosition((radius - (a % en)).toInt() + xOffset, radius.toInt() + zOffset)
            3 -> ChunkPosition(-radius.toInt() + xOffset, (radius - (a % en)).toInt() + zOffset)
            else -> ChunkPosition.ZERO
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

const val CHUNK_DATA_VERSION = 2578
