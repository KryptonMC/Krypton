/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world

import net.kyori.adventure.key.Key.key
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.GameVersion
import org.kryptonmc.api.world.rule.GameRuleHolder
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityProperties
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnLivingEntity
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.csv.csv
import org.kryptonmc.krypton.util.profiling.Profiler
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector3i
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.copyTo
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.moveTo
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

data class KryptonWorld(
    override val server: KryptonServer,
    override val folder: Path,
    override val uuid: UUID,
    override val name: String,
    val allowCheats: Boolean,
    private val borderBuilder: BorderBuilder,
    var clearWeatherTime: Int,
    var dayTime: Long,
    override val difficulty: Difficulty,
    //val endDimensionData: EndDimensionData, // for the end, when it is supported
    override val gameRules: GameRuleHolder,
    val generationSettings: WorldGenerationSettings,
    override var gamemode: Gamemode,
    override val isHardcore: Boolean,
    //val isInitialized: Boolean, // we always assume this is a complete world
    val lastPlayed: LocalDateTime,
    val mapFeatures: Boolean,
    override var isRaining: Boolean,
    var rainTime: Int,
    override val spawnLocation: Vector3i,
    val spawnAngle: Float,
    override var isThundering: Boolean,
    var thunderTime: Int,
    override var time: Long,
    val nbtVersion: Int,
    override val version: GameVersion,
    override val maxHeight: Int,
    val serverBrands: MutableSet<String>,
) : World {

    override val seed = generationSettings.seed

    override val chunks: MutableSet<KryptonChunk> = ConcurrentHashMap.newKeySet()
    val players: MutableSet<KryptonPlayer> = ConcurrentHashMap.newKeySet()
    val entities: MutableSet<KryptonEntity> = ConcurrentHashMap.newKeySet()

    val chunkManager = ChunkManager(this)
    val dimension = key("overworld")

    private var oldRainLevel = 0F
    override var rainLevel = 0F
    private var oldThunderLevel = 0F
    override var thunderLevel = 0F

    override val border = KryptonWorldBorder(
        this,
        borderBuilder.size,
        Vector2d(borderBuilder.centerX, borderBuilder.centerZ),
        borderBuilder.damagePerBlock,
        borderBuilder.safeZone,
        borderBuilder.sizeLerpTarget,
        borderBuilder.sizeLerpTime,
        borderBuilder.warningBlocks,
        borderBuilder.warningTime
    )

    override fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vector) {
        // TODO: Fix this when the rest of the entity types exist again
//        require(type != EntityType.MARKER) { "Markers cannot be spawned!" }
//        when (type) {
//            EntityType.PLAYER -> return // TODO: Implement player spawning
//            EntityType.EXPERIENCE_ORB -> spawnExperienceOrb(location)
//            EntityType.PAINTING -> spawnPainting(location)
//        }
        val entity = EntityFactory.create(type, server, uuid)?.apply { this.location = location.toLocation(0F, 0F) } ?: return
        val packets = mutableListOf(
            if (entity is KryptonLivingEntity) PacketOutSpawnLivingEntity(entity) else PacketOutSpawnEntity(entity),
            PacketOutEntityMetadata(entity.id, entity.data.all)
        )
        if (entity is KryptonLivingEntity) packets += PacketOutEntityProperties(entity.id, entity.attributes.syncableAttributes)
        players.forEach { player -> packets.forEach { player.session.sendPacket(it) } }
        entities += entity
        return
    }

    override fun spawnExperienceOrb(location: Vector) = Unit // TODO: Implement XP orb spawning

    override fun spawnPainting(location: Vector) = Unit // TODO: Implement painting spawning

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
        val raining = isRaining
        if (gameRules[GameRules.DO_WEATHER_CYCLE]) {
            if (clearWeatherTime > 0) {
                --clearWeatherTime
                thunderTime = if (isThundering) 0 else 1
                rainTime = if (isRaining) 0 else 1
                isThundering = false
                isRaining = false
            } else {
                profiler.push("calculate thunder")
                if (thunderTime > 0) {
                    if (thunderTime-- == 0) isThundering = !isThundering
                } else {
                    thunderTime = if (isThundering) Random.nextInt(12_000) + 3600 else Random.nextInt(168_000) + 12_000
                }
                profiler.pop()
                profiler.push("calculate rain")
                if (rainTime > 0) {
                    if (rainTime-- == 0) isRaining = !isRaining
                } else {
                    rainTime = Random.nextInt(if (isRaining) 12_000 else 168_000) + 12_000
                }
                profiler.pop()
            }
        }
        profiler.push("set levels")
        oldThunderLevel = thunderLevel
        thunderLevel = if (isThundering) (thunderLevel + 0.01).toFloat() else (thunderLevel - 0.01).toFloat()
        thunderLevel = min(max(thunderLevel, 0F), 1F)
        oldRainLevel = rainLevel
        rainLevel = if (isRaining) (rainLevel + 0.01).toFloat() else (rainLevel - 0.01).toFloat()
        rainLevel = min(max(rainLevel, 0F), 1F)
        profiler.pop()

        profiler.push("broadcast weather changes")
        if (oldRainLevel != rainLevel) {
            val rainPacket = PacketOutChangeGameState(GameState.RAIN_LEVEL_CHANGE, rainLevel)
            players.forEach { it.session.sendPacket(rainPacket) }
        }
        if (oldThunderLevel != thunderLevel) {
            val thunderPacket = PacketOutChangeGameState(GameState.THUNDER_LEVEL_CHANGE, thunderLevel)
            players.forEach { it.session.sendPacket(thunderPacket) }
        }
        if (raining != isRaining) {
            if (raining) {
                val rainPacket = PacketOutChangeGameState(GameState.END_RAINING)
                players.forEach { it.session.sendPacket(rainPacket) }
            } else {
                val rainPacket = PacketOutChangeGameState(GameState.BEGIN_RAINING)
                players.forEach { it.session.sendPacket(rainPacket) }
            }
            val rainLevelPacket = PacketOutChangeGameState(GameState.RAIN_LEVEL_CHANGE, rainLevel)
            val thunderLevelPacket = PacketOutChangeGameState(GameState.THUNDER_LEVEL_CHANGE, thunderLevel)
            players.forEach {
                it.session.sendPacket(rainLevelPacket)
                it.session.sendPacket(thunderLevelPacket)
            }
        }
        profiler.pop()
        profiler.pop()

        profiler.push("chunk tick")
        chunks.forEach { chunk -> chunk.tick(players.count { it.location in chunk.position }) }
        profiler.pop()
    }

    fun saveDebugReport(path: Path) {
        val chunksPath = path.resolve("chunks.csv")
        Files.newBufferedWriter(chunksPath).use { it.dumpChunks() }
    }

    override fun save() {
        val gamerules = gameRules.rules.transform { (rule, value) -> rule.name to StringBinaryTag.of(value.toString()) }
        val dimensions = generationSettings.dimensions.transform { (key, value) -> key.toString() to value.toNBT() }

        val data = CompoundBinaryTag.builder().put("Data", CompoundBinaryTag.builder()
            .putBoolean("allowCommands", false)
            .putDouble("BorderCenterX", border.center.x())
            .putDouble("BorderCenterZ", border.center.y())
            .putDouble("BorderDamagePerBlock", border.damageMultiplier)
            .putDouble("BorderSize", border.size)
            .putDouble("BorderSafeZone", border.safeZone)
            .putDouble("BorderSizeLerpTarget", border.sizeLerpTarget)
            .putLong("BorderSizeLerpTime", border.sizeLerpTime)
            .putDouble("BorderWarningBlocks", border.warningBlocks.toDouble())
            .putDouble("BorderWarningTime", border.warningTime.toDouble())
            .putInt("clearWeatherTime", clearWeatherTime)
            .put("CustomBossEvents", CompoundBinaryTag.empty())
            .put("DataPacks", CompoundBinaryTag.builder()
                .put("Enabled", ListBinaryTag.of(BinaryTagTypes.STRING, listOf(StringBinaryTag.of("vanilla"))))
                .put("Disabled", ListBinaryTag.empty())
                .build())
            .putInt("DataVersion", ServerInfo.WORLD_VERSION)
            .putLong("DayTime", dayTime)
            .putByte("Difficulty", difficulty.ordinal.toByte())
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
            .put("ServerBrands", ListBinaryTag.from(serverBrands.apply { add(KryptonServerInfo.name) }.map(StringBinaryTag::of)))
            .putInt("SpawnX", spawnLocation.x())
            .putInt("SpawnY", spawnLocation.y())
            .putInt("SpawnZ", spawnLocation.z())
            .putBoolean("thundering", isThundering)
            .putLong("Time", time)
            .putInt("version", NBT_VERSION)
            .put("Version", CompoundBinaryTag.builder()
                .putInt("Id", ServerInfo.GAME_VERSION.id)
                .putString("Name", ServerInfo.GAME_VERSION.name)
                .putBoolean("Snapshot", ServerInfo.GAME_VERSION.isSnapshot)
                .build())
            .putInt("WanderingTraderSpawnChance", 25)
            .putInt("WanderingTraderSpawnDelay", 24_000)
            .build()).build()

        val temp = folder.createTempFile("level", ".dat")
        BinaryTagIO.writer().write(data, temp, BinaryTagIO.Compression.GZIP)
        val dataPath = folder.resolve("level.dat")
        if (!dataPath.exists()) {
            temp.copyTo(dataPath)
            return
        }
        val oldDataPath = folder.resolve("level.dat_old").apply { deleteIfExists() }
        dataPath.moveTo(oldDataPath)
        dataPath.deleteIfExists()
        temp.moveTo(dataPath)
    }

    override fun audiences() = players

    override fun equals(other: Any?) = other is KryptonWorld && uuid == other.uuid

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

const val NBT_VERSION = 19_133
