package org.kryptonmc.krypton.world

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
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.UUID

data class KryptonWorld(
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

object LevelDataVersion {

    const val ID = 2584
    const val NAME = "1.16.5"
    const val SNAPSHOT = false
}
