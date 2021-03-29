package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.*
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.WorldVersion
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.world.bossbar.Bossbar
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import java.time.LocalDateTime

data class KryptonWorld(
    override val name: String,
    override val chunks: MutableSet<KryptonChunk>,
    val bossbars: List<Bossbar>,
    val allowCheats: Boolean,
    val borderBuilder: BorderBuilder,
    val clearWeatherTime: Int,
    var dayTime: Long,
    override val difficulty: Difficulty,
    val difficultyLocked: Boolean,
    //val endDimensionData: EndDimensionData,
    //val gamerules: List<Gamerule<*>>,
    val worldGenSettings: WorldGenerationSettings,
    override var gamemode: Gamemode,
    override val isHardcore: Boolean,
    //val isInitialized: Boolean,
    val lastPlayed: LocalDateTime,
    val mapFeatures: Boolean,
    var isRaining: Boolean,
    var rainTime: Int,
    val randomSeed: Long,
    val spawnLocationBuilder: LocationBuilder,
    val isThundering: Boolean,
    val thunderTime: Int,
    override var time: Long,
    val nbtVersion: Int,
    override val version: WorldVersion,
    override val maxHeight: Int,
    override val seed: Long,
    val players: MutableList<KryptonPlayer>
) : World {

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

    fun tick() {
        if (players.isEmpty()) return // don't tick the world if there's no players in it

        // tick time
        time++
        dayTime++

        // tick rain
        // TODO: Actually add in some probabilities and calculations for rain and thunder storms
        if (rainTime > 0) {
            if (!isRaining) isRaining = true
            rainTime--
            return
        }

        // this ensures the game state change to signal we've stopped raining only happens once
        if (isRaining) {
            isRaining = false
            val endRainPacket = PacketOutChangeGameState(GameState.END_RAINING)
            players.forEach { it.session.sendPacket(endRainPacket) }
        }
    }
}

const val NBT_DATA_VERSION = 2584

// TODO: Use this in MCA file writing
//object LevelDataVersion {
//
//    const val ID = 2584
//
//    const val NAME = "1.16.5"
//
//    const val SNAPSHOT = false
//}