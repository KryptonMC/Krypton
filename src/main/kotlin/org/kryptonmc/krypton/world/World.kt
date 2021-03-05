package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.space.Position
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import java.time.LocalDateTime

data class World(
    val name: String,
    //val bossbars: List<Bossbar>,
    //val allowCheats: Boolean,
    val border: WorldBorder,
    //val clearWeatherTime: Int,
    var dayTime: Long,
    val difficulty: Difficulty,
    val difficultyLocked: Boolean,
    //val endDimensionData: EndDimensionData,
    //val gamerules: List<Gamerule<*>>,
    val worldGenSettings: WorldGenerationSettings,
    var gameType: Gamemode,
    //val isHardcore: Boolean,
    //val isInitialized: Boolean,
    val lastPlayed: LocalDateTime,
    //val mapFeatures: Boolean,
    //val isRaining: Boolean,
    //val rainTime: Int,
    //val randomSeed: Long,
    val spawnPosition: Position,
    //val isThundering: Boolean,
    //val thunderTime: Int,
    var time: Long
)

// TODO: Use this in MCA file writing
//object LevelDataVersion {
//
//    const val ID = 2584
//
//    const val NAME = "1.16.5"
//
//    const val SNAPSHOT = false
//}