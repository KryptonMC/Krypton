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

import com.mojang.serialization.Codec
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.event.entity.EntityRemoveEvent
import org.kryptonmc.api.event.entity.EntitySpawnEvent
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameVersion
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.util.csv.csv
import org.kryptonmc.krypton.util.profiling.Profiler
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.effect.Effect
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.forEachInRange
import org.kryptonmc.krypton.util.lerp
import org.kryptonmc.krypton.util.synchronize
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.data.WorldData
import org.kryptonmc.krypton.world.storage.WorldDataAccess
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector3i
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

class KryptonWorld(
    override val server: KryptonServer,
    private val storageAccess: WorldDataAccess,
    val data: WorldData,
    override val dimension: ResourceKey<World>,
    override val dimensionType: DimensionType,
    val isDebug: Boolean,
    override val seed: Long,
    private val tickTime: Boolean
) : World, BlockAccessor {

    private val random = Random()
    val registryHolder = server.registryHolder

    override val border = KryptonWorldBorder(0.0, Vector2d(0.0, 0.0), 0.0, 0.0, 0.0, 0L, 0, 0) // FIXME
    override val gamemode: Gamemode
        get() = data.gamemode
    override val difficulty: Difficulty
        get() = data.difficulty
    override val gameRules: KryptonGameRuleHolder
        get() = data.gameRules
    override val isHardcore: Boolean
        get() = data.isHardcore
    override val isRaining: Boolean
        get() = getRainLevel(1F) > 0.2F
    override val isThundering: Boolean
        get() = if (dimensionType.hasSkylight && !dimensionType.hasCeiling) getThunderLevel(1F) > 0.9F else false
    override val name: String
        get() = data.name
    override val spawnLocation: Vector3i
        get() = Vector3i(data.spawnX, data.spawnY, data.spawnZ)
    override val time: Long
        get() = data.time
    override val version: GameVersion
        get() = ServerInfo.GAME_VERSION
    override val folder = storageAccess.path

    val chunkMap = Long2ObjectOpenHashMap<KryptonChunk>().synchronize()
    override val chunks: Collection<KryptonChunk>
        get() = chunkMap.values
    val players: MutableSet<KryptonPlayer> = ConcurrentHashMap.newKeySet()
    val entities: MutableSet<KryptonEntity> = ConcurrentHashMap.newKeySet()
    private val entitiesByChunk = object : ConcurrentHashMap<Long, MutableSet<KryptonEntity>>() {
        override fun get(key: Long) = super.computeIfAbsent(key) { newKeySet() }
        operator fun get(chunk: KryptonChunk) = get(ChunkPosition.toLong(chunk.position.x, chunk.position.z))
    }

    val chunkManager = ChunkManager(this)
    val playerManager = server.playerManager

    override val height = dimensionType.height
    override val minimumBuildHeight = dimensionType.minimumY

    private var oldRainLevel = 0F
    override var rainLevel = 0F
        set(value) {
            val clamped = value.clamp(0F, 1F)
            oldRainLevel = clamped
            field = clamped
        }
    private var oldThunderLevel = 0F
    override var thunderLevel = 0F
        set(value) {
            val clamped = value.clamp(0F, 1F)
            oldRainLevel = clamped
            field = clamped
        }
    private var skyDarken = 0

    @Suppress("UNCHECKED_CAST")
    override fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vector): T? {
        if (!type.isSummonable) return null
        // TODO: Fix this when the rest of the entity types exist again
//        when (type) {
//            EntityType.PLAYER -> return // TODO: Implement player spawning
//            EntityType.EXPERIENCE_ORB -> spawnExperienceOrb(location)
//            EntityType.PAINTING -> spawnPainting(location)
//        }
        val entity = EntityFactory.create(type, this)?.apply { this.location = location.toLocation(0F, 0F) } ?: return null
        spawnEntity(entity)
        return entity as? T
    }

    fun spawnEntity(entity: KryptonEntity) {
        if (entity.world != this) return
        server.eventManager.fire(EntitySpawnEvent(entity, this)).thenAccept { event ->
            if (!event.result.isAllowed) return@thenAccept
            val location = entity.location

            if (entity is KryptonPlayer) {
                // TODO: World border
                entity.session.sendPacket(PacketOutTimeUpdate(data.time, data.dayTime, data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]))
            }
            forEachInRange(location, server.config.world.viewDistance) {
                if (entity is KryptonPlayer) it.addViewer(entity)
                if (it is KryptonPlayer) entity.addViewer(it)
            }

            val chunk = getChunk(entity.location) ?: return@thenAccept
            entitiesByChunk[chunk.position.toLong()].add(entity)
            entities.add(entity)
        }
    }

    fun removeEntity(entity: KryptonEntity) {
        if (entity.world != this) return
        server.eventManager.fire(EntityRemoveEvent(entity, this)).thenAccept {
            if (!it.result.isAllowed) return@thenAccept
            entity.viewers.forEach(entity::removeViewer)
            val chunk = getChunk(entity.location) ?: return@thenAccept
            entitiesByChunk[chunk.position.toLong()].remove(entity)
            entities.remove(entity)
        }
    }

    override fun spawnExperienceOrb(location: Vector) = Unit // TODO: Implement XP orb spawning

    override fun spawnPainting(location: Vector) = Unit // TODO: Implement painting spawning

    fun playEffect(effect: Effect, position: Vector3i, data: Int, except: KryptonPlayer) = playerManager.broadcast(PacketOutEffect(effect, position, data, false), this, position, 64.0, except)

    fun playSound(
        position: Vector3i,
        sound: Sound,
        event: SoundEvent,
        except: KryptonPlayer
    ) = playSound(position.x().toDouble() + 0.5, position.y().toDouble() + 0.5, position.z().toDouble() + 0.5, sound, event, except)

    private fun playSound(
        x: Double,
        y: Double,
        z: Double,
        sound: Sound,
        event: SoundEvent,
        except: KryptonPlayer
    ) = playerManager.broadcast(PacketOutSoundEffect(sound, event, x, y, z), this, x, y, z, if (sound.volume() > 1F) (16F * sound.volume()).toDouble() else 16.0, except)

    override fun getBlock(x: Int, y: Int, z: Int): Block {
        if (y.outsideBuildHeight) return Blocks.VOID_AIR
        val chunk = getChunkAt(x, z) ?: return Blocks.AIR
        return chunk.getBlock(x, y, z)
    }

    override fun getBlock(position: Vector3i) = getBlock(position.x(), position.y(), position.z())

    override fun getChunkAt(x: Int, z: Int) = chunkManager[x, z]

    override fun getChunk(x: Int, y: Int, z: Int) = getChunkAt(x shr 4, z shr 4)

    override fun getChunk(position: Position) = getChunk(position.blockX, position.blockY, position.blockZ)

    override fun getChunk(position: Vector3i) = getChunk(position.x(), position.y(), position.z())

    fun getEntitiesInChunk(chunk: KryptonChunk) = entitiesByChunk[chunk.position.toLong()]

    override fun loadChunk(x: Int, z: Int) = chunkManager.load(x, z)

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        if (y.outsideBuildHeight) return
        getChunkAt(x shr 4, z shr 4)?.setBlock(x, y, z, block)?.run {
            playerManager.sendToAll(PacketOutBlockChange(block, x, y, z), this@KryptonWorld)
        }
    }

    override fun setBlock(position: Vector3i, block: Block) = setBlock(position.x(), position.y(), position.z(), block)

    fun tick(profiler: Profiler) {
        if (players.isEmpty()) return // don't tick the world if there's no players in it

        // tick rain
        profiler.push("weather")
        val wasRaining = isRaining
        if (dimensionType.hasSkylight) {
            if (gameRules[GameRules.DO_WEATHER_CYCLE]) {
                var clearWeatherTime = data.clearWeatherTime
                var thunderTime = data.thunderTime
                var rainTime = data.rainTime
                var thundering = data.isThundering
                var isRaining = data.isRaining
                if (clearWeatherTime > 0) {
                    --clearWeatherTime
                    thunderTime = if (thundering) 0 else 1
                    rainTime = if (isRaining) 0 else 1
                    thundering = false
                    isRaining = false
                } else {
                    if (thunderTime > 0) {
                        thunderTime--
                        if (thunderTime == 0) thundering = !thundering
                    } else if (thundering) {
                        thunderTime = random.nextInt(12000) + 3600
                    } else {
                        thunderTime = random.nextInt(168000) + 12000
                    }
                    if (rainTime > 0) {
                        rainTime--
                        if (rainTime == 0) isRaining = !isRaining
                    } else if (isRaining) {
                        rainTime = random.nextInt(12000) + 12000
                    } else {
                        rainTime = random.nextInt(168000) + 12000
                    }
                }
                data.thunderTime = thunderTime
                data.rainTime = rainTime
                data.clearWeatherTime = clearWeatherTime
                data.isThundering = thundering
                data.isRaining = isRaining
            }
            oldThunderLevel = thunderLevel
            thunderLevel = if (data.isThundering) thunderLevel + 0.01F else thunderLevel - 0.01F
            thunderLevel = thunderLevel.clamp(0F, 1F)
            oldRainLevel = rainLevel
            rainLevel = if (data.isRaining) rainLevel + 0.01F else rainLevel - 0.01F
            rainLevel = rainLevel.clamp(0F, 1F)
        }

        if (oldRainLevel != rainLevel) playerManager.sendToAll(PacketOutChangeGameState(GameState.RAIN_LEVEL_CHANGE, rainLevel), this)
        if (oldThunderLevel != thunderLevel) playerManager.sendToAll(PacketOutChangeGameState(GameState.THUNDER_LEVEL_CHANGE, thunderLevel), this)
        if (wasRaining != isRaining) {
            playerManager.sendToAll(PacketOutChangeGameState(if (wasRaining) GameState.END_RAINING else GameState.BEGIN_RAINING))
            playerManager.sendToAll(PacketOutChangeGameState(GameState.RAIN_LEVEL_CHANGE, rainLevel))
            playerManager.sendToAll(PacketOutChangeGameState(GameState.THUNDER_LEVEL_CHANGE, thunderLevel))
        }
        profiler.pop()

        // TODO: Sky brightness
        tickTime()

        profiler.push("chunk tick")
        chunkMap.values.forEach { chunk -> chunk.tick(players.count { it.location in chunk.position }) }
        profiler.pop()
    }

    private fun tickTime() {
        if (!tickTime) return
        data.time++
        // TODO: Tick scheduled events
        if (data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]) data.dayTime++
    }

    fun saveDebugReport(path: Path) {
        val chunksPath = path.resolve("chunks.csv")
        Files.newBufferedWriter(chunksPath).use { it.dumpChunks() }
    }

    override fun save() = chunkManager.saveAll()

    override fun audiences() = players

    private fun Writer.dumpChunks() {
        val output = csv(this) {
            plus("x")
            plus("z")
            plus("world")
        }
        chunkMap.values.forEach { output.writeRow(it.position.x, it.position.z, it.world) }
    }

    fun getRainLevel(delta: Float) = lerp(delta, oldRainLevel, rainLevel)

    fun getThunderLevel(delta: Float) = lerp(delta, oldThunderLevel, thunderLevel) * getRainLevel(delta)

    companion object {

        val RESOURCE_KEY_CODEC: Codec<ResourceKey<World>> = KEY_CODEC.xmap({ ResourceKey.of(ResourceKeys.DIMENSION, it) }, ResourceKey<World>::location)
        val OVERWORLD = ResourceKey.of(InternalResourceKeys.WORLD, DimensionTypes.OVERWORLD_KEY.location)
        val THE_NETHER = ResourceKey.of(InternalResourceKeys.WORLD, DimensionTypes.NETHER_KEY.location)
        val THE_END = ResourceKey.of(InternalResourceKeys.WORLD, DimensionTypes.END_KEY.location)
    }
}

const val NBT_VERSION = 19_133
