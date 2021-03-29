package org.kryptonmc.krypton.world

import com.github.benmanes.caffeine.cache.Caffeine
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.nbt.*
import net.kyori.adventure.nbt.BinaryTagIO.Compression.*
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.*
import org.kryptonmc.krypton.config.WorldConfig
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.AttributeKey
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.entity.entities.data.InventoryItem
import org.kryptonmc.krypton.entity.entities.data.PlayerAbilities
import org.kryptonmc.krypton.entity.entities.data.PlayerData
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.world.chunk.*
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.generation.toGenerator
import java.io.File
import java.io.FileInputStream
import java.io.IOException
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

    private val folder = File(CURRENT_DIRECTORY, config.name)
    private val regionFolder = File(folder, "/region")
    private val playerDataFolder = File(folder, "/playerdata").apply { mkdir() }

    private val regionCache = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build<Vector, MutableMap<Vector, KryptonChunk>>()

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
            nbt.getLong("RandomSeed"),
            mutableListOf()
        )
    }

    fun loadPlayerData(uuid: UUID): PlayerData? {
        val playerFile = File(playerDataFolder, "$uuid.dat")
        if (!playerFile.exists()) {
            playerFile.createNewFile()
            return null
        }

        val nbt = try {
            BinaryTagIO.unlimitedReader().read(playerFile.toPath(), GZIP)
        } catch (exception: IOException) {
            return null
        }

        val attributes = nbt.getList("Attributes").asSequence()
            .map { it as CompoundBinaryTag }
            .map { Attribute(AttributeKey.fromKey(it.getString("Name").toNamespacedKey()), it.getDouble("Base")) }
            .toList()

        val abilities = nbt.getCompound("abilities").let {
            PlayerAbilities(
                it.getBoolean("instabuild"),
                it.getBoolean("mayfly"),
                it.getBoolean("invulnerable"),
                it.getBoolean("mayBuild"),
                it.getBoolean("flying"),
                it.getFloat("walkSpeed"),
                it.getFloat("flySpeed")
            )
        }

        val inventory = nbt.getList("Inventory").asSequence()
            .map { it as CompoundBinaryTag }
            .map {
                InventoryItem(
                    it.getByte("Slot"),
                    it.getString("id").toNamespacedKey(),
                    it.getCompound("tag"),
                    it.getByte("Count")
                )
            }
            .toList()

        val position = nbt.getList("Pos").map { (it as DoubleBinaryTag).value() }
        val rotation = nbt.getList("Rotation").map { (it as FloatBinaryTag).value() }

        return PlayerData(
            nbt.getShort("SleepTimer"),
            nbt.getBoolean("SpawnForced"),
            attributes,
            nbt.getBoolean("Invulnerable"),
            nbt.getFloat("AbsorptionAmount"),
            abilities,
            nbt.getFloat("FallDistance"),
            nbt.getShort("Air", 300),
            Vector(nbt.getInt("SpawnX"), nbt.getInt("SpawnY"), nbt.getInt("SpawnZ")),
            nbt.getFloat("SpawnAngle"),
            nbt.getInt("Score"),
            Vector(position[0], position[1], position[2]),
            Gamemode.fromId(nbt.getInt("previousPlayerGameType", -1)),
            Gamemode.fromId(nbt.getInt("playerGameType", 0)) ?: Gamemode.SURVIVAL,
            nbt.getInt("SelectedItemSlot"),
            nbt.getShort("HurtTime"),
            inventory,
            nbt.getBoolean("FallFlying"),
            nbt.getString("SpawnDimension", "minecraft:overworld").toNamespacedKey(),
            nbt.getString("Dimension", "minecraft:overworld").toNamespacedKey(),
            nbt.getFloat("Health", 1.0F),
            nbt.getBoolean("OnGround"),
            rotation[1],
            rotation[0]
        )
    }

    fun savePlayerData(player: KryptonPlayer) {
        val playerFile = File(playerDataFolder, "${player.uuid}.dat").apply { createNewFile() }

        val attributes = player.data.attributes.map {
            CompoundBinaryTag.builder()
                .putDouble("Base", it.value)
                .putString("Name", it.key.key.toString())
                .build()
        }

        BinaryTagIO.writer().write(CompoundBinaryTag.builder()
            .put(player.data.brain.write())
            .putShort("SleepTimer", player.data.sleepTimer)
            .putBoolean("SpawnForced", player.data.spawnForced)
            .put("Attributes", ListBinaryTag.from(attributes))
            .putBoolean("Invulnerable", player.data.isInvulnerable)
            .putFloat("AbsorptionAmount", player.data.absorptionAmount)
            .put("abilities", CompoundBinaryTag.builder()
                .putFloat("walkSpeed", 0.1F)
                .putBoolean("instabuild", player.abilities.isCreativeMode)
                .putBoolean("mayfly", player.abilities.canFly)
                .putBoolean("invulnerable", player.abilities.isInvulnerable)
                .putBoolean("mayBuild", player.gamemode != Gamemode.ADVENTURE && player.gamemode != Gamemode.SPECTATOR)
                .putBoolean("flying", player.isFlying)
                .putFloat("flySpeed", player.abilities.flyingSpeed)
                .build())
            .putFloat("FallDistance", player.data.fallDistance)
            .put("UUID", player.uuid.serialize())
            .putInt("SpawnX", player.world.spawnLocation.x.toInt())
            .putInt("SpawnY", player.world.spawnLocation.y.toInt())
            .putInt("SpawnZ", player.world.spawnLocation.z.toInt())
            .putFloat("SpawnAngle", player.data.spawnAngle)
            .putShort("Air", player.data.air)
            .putInt("Score", player.data.score)
            .put("Pos", ListBinaryTag.builder()
                .add(DoubleBinaryTag.of(player.location.x))
                .add(DoubleBinaryTag.of(player.location.y))
                .add(DoubleBinaryTag.of(player.location.z))
                .build())
            .putInt("previousPlayerGameType", player.data.previousGamemode?.ordinal ?: -1)
            .putInt("DataVersion", NBT_DATA_VERSION)
            .putInt("SelectedItemSlot", player.data.selectedItemSlot)
            .putShort("HurtTime", player.data.hurtTime)
            .put("Inventory", ListBinaryTag.of(BinaryTagTypes.COMPOUND, player.data.inventory.map { it.toNBT() }))
            .putBoolean("FallFlying", player.data.fallFlying)
            .putInt("playerGameType", player.gamemode.ordinal)
            .putString("SpawnDimension", player.data.spawnDimension.toString())
            .putFloat("Health", player.data.health)
            .putBoolean("OnGround", player.isOnGround)
            .putString("Dimension", player.data.dimension.toString())
            .put("Rotation", ListBinaryTag.builder()
                .add(FloatBinaryTag.of(player.location.pitch))
                .add(FloatBinaryTag.of(player.location.yaw))
                .build())
            .build(), playerFile.toPath(), GZIP)
    }

    fun loadChunks(positions: List<Vector>): Map<Vector, KryptonChunk> {
        val chunks = mutableMapOf<Vector, KryptonChunk>()
        val groupedByRegion = mutableMapOf<Vector, MutableList<Vector>>()

        positions.forEach {
            val regionX = floor(it.x / 32.0)
            val regionZ = floor(it.z / 32.0)

            groupedByRegion.getOrPut(Vector(regionX, 0.0, regionZ)) { mutableListOf() } += it
        }

        groupedByRegion.forEach { (key, value) ->
            val cachedChunks = regionCache.getIfPresent(key)
            if (cachedChunks != null) {
                chunks += cachedChunks
                return@forEach
            }

            val region = loadRegion(regionFolder, key.x.toInt(), key.z.toInt(), value)
            chunks += region.chunks
            regionCache.put(key, region.chunks.toMutableMap())
        }

        return chunks
    }

    private fun loadRegion(folder: File, x: Int, z: Int, positions: List<Vector>): Region {
        val file = RandomAccessFile(File(folder, "r.$x.$z.mca"), "r")
        val chunks = mutableMapOf<Vector, KryptonChunk>()

        repeat(1024) {
            file.seek(it * 4L)
            var offset = file.read() shl 16
            offset = offset or ((file.read() and 0xFF) shl 8)
            offset = offset or (file.read() and 0xFF)
            if (file.readByte() == 0.toByte()) return@repeat

            file.seek(4096L + it * 4)
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

        val location = Vector(nbt.getInt("xPos").toDouble(), 0.0, nbt.getInt("zPos").toDouble())
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
                nbtSection.getByteArray("BlockLight").toList(),
                nbtSection.getByteArray("SkyLight").toList(),
                palette,
                nbtSection.getLongArray("BlockStates").toList()
            )
        }

        return location to KryptonChunk(
            location,
            sections,
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            nbt.getLong("LastUpdate"),
            nbt.getLong("inhabitedTime"),
            Heightmaps(motionBlocking, oceanFloor, worldSurface)
        )
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

        var x = 0.0
        var z = 0.0

        when (floor(a / (radius * 2)).toInt()) {
            // find the face (0 = top, 1 = right, 2 = bottom, 3 = left)
            0 -> {
                x = a - radius
                z = -radius
            }
            1 -> {
                x = radius
                z = (a % en) - radius
            }
            2 -> {
                x = radius - (a % en)
                z = radius
            }
            3 -> {
                x = -radius
                z = radius - (a % en)
            }
        }

        return Vector(x + xOffset, 0.0, z + zOffset)
    }

    companion object {

        private const val DEFAULT_BUILD_LIMIT = 256

        private val LOGGER = logger<KryptonWorldManager>()
    }
}

private fun UUID.serialize() = IntArrayBinaryTag.of(
    (mostSignificantBits shr 32).toInt(),
    (mostSignificantBits and Int.MAX_VALUE.toLong()).toInt(),
    (leastSignificantBits shr 32).toInt(),
    (leastSignificantBits and Int.MAX_VALUE.toLong()).toInt()
)