package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.*
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.WorldVersion
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
    val isRaining: Boolean,
    val rainTime: Int,
    val randomSeed: Long,
    val spawnLocationBuilder: LocationBuilder,
    val isThundering: Boolean,
    val thunderTime: Int,
    override var time: Long,
    val nbtVersion: Int,
    override val version: WorldVersion,
    override val maxHeight: Int,
    override val seed: Long
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
        time++
        dayTime++
    }
}

// TODO: Use this in MCA file writing
//object LevelDataVersion {
//
//    const val ID = 2584
//
//    const val NAME = "1.16.5"
//
//    const val SNAPSHOT = false
//}