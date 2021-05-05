package org.kryptonmc.krypton.world

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.WorldVersion
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.util.csv.csv
import org.kryptonmc.krypton.util.profiling.Profiler
import org.kryptonmc.krypton.world.bossbar.Bossbar
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

data class KryptonWorld(
    override val server: KryptonServer,
    val folder: Path,
    val uuid: UUID,
    override val name: String,
    override val chunks: MutableSet<KryptonChunk>,
    val bossbars: List<Bossbar>,
    val allowCheats: Boolean,
    private val borderBuilder: BorderBuilder,
    val clearWeatherTime: Int,
    var dayTime: Long,
    override val difficulty: Difficulty,
    val difficultyLocked: Boolean,
    //val endDimensionData: EndDimensionData, // for the end, when it is supported
    val gamerules: Map<Gamerule, String>, // everything is a string because Mojang :fingerguns:
    val generationSettings: WorldGenerationSettings,
    override var gamemode: Gamemode,
    override val isHardcore: Boolean,
    //val isInitialized: Boolean, // we always assume this is a complete world
    val lastPlayed: LocalDateTime,
    val mapFeatures: Boolean,
    var isRaining: Boolean,
    var rainTime: Int,
    val randomSeed: Long,
    private val spawnLocationBuilder: LocationBuilder,
    val isThundering: Boolean,
    val thunderTime: Int,
    override var time: Long,
    val nbtVersion: Int,
    override val version: WorldVersion,
    override val maxHeight: Int,
    override val seed: Long,
    val players: MutableList<KryptonPlayer>,
    val serverBrands: Set<String>
) : World {

    val chunkManager = ChunkManager(this)
    val dimension = NamespacedKey(value = "overworld")

    override val border = KryptonWorldBorder(
        this,
        borderBuilder.size,
        Location(this, borderBuilder.centerX, 0.0, borderBuilder.centerZ),
        borderBuilder.damagePerBlock,
        borderBuilder.safeZone,
        borderBuilder.sizeLerpTarget,
        borderBuilder.sizeLerpTime,
        borderBuilder.warningBlocks,
        borderBuilder.warningTime
    )

    override val spawnLocation = Location(
        this,
        spawnLocationBuilder.x,
        spawnLocationBuilder.y,
        spawnLocationBuilder.z
    )

    fun tick(profiler: Profiler) {
        if (players.isEmpty()) return // don't tick the world if there's no players in it

        profiler.push("time tick")
        // tick time
        time++
        dayTime++
        profiler.pop()

        // tick rain
        // TODO: Actually add in some probabilities and calculations for rain and thunder storms
        profiler.push("weather")
        if (rainTime > 0) {
            if (!isRaining) isRaining = true
            rainTime--
            return
        }

        // this ensures the game state change to signal we've stopped raining only happens once
        if (isRaining) {
            profiler.push("rain update")
            isRaining = false
            val endRainPacket = PacketOutChangeGameState(GameState.END_RAINING)
            players.forEach { it.session.sendPacket(endRainPacket) }
            profiler.pop()
        }
        profiler.pop()

        profiler.push("chunk tick")
        chunks.forEach { chunk -> chunk.tick(players.filter { it.location in chunk.position }.size) }
        profiler.pop()
    }

    fun saveDebugReport(path: Path) {
        val chunksPath = path.resolve("chunks.csv")
        Files.newBufferedWriter(chunksPath).use { it.dumpChunks() }
    }

    override fun save() {
        val dataPath = folder.resolve("level.dat")
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
            .putInt("DataVersion", version.id)
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
            .putString("Krypton.Version", KryptonServer.KryptonServerInfo.version)
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
                .putInt("Id", version.id)
                .putString("Name", version.name)
                .putBoolean("Snapshot", version.isSnapshot)
                .build())
            .putInt("WanderingTraderSpawnChance", 25)
            .putInt("WanderingTraderSpawnDelay", 24000)
            .build()).build(), dataPath, BinaryTagIO.Compression.GZIP)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KryptonWorld
        return uuid == other.uuid
    }

    override fun hashCode() = uuid.hashCode()

    override fun toString() = "KryptonWorld(uuid=$uuid,name=$name)"

    private fun Writer.dumpChunks() {
        val output = csv(this) {
            plus("x")
            plus("z")
            plus("world")
        }
        chunks.forEach { output.writeRow(it.position.x, it.position.z, it.world) }
    }
}

const val NBT_DATA_VERSION = 2584
const val NBT_VERSION = 19133
