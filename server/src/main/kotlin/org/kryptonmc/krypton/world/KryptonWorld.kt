/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.effect.sound.calculateDistance
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.EntityManager
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.SessionManager
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.out.play.GameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetBlockDestroyStage
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutWorldEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.data.WorldData
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.nio.file.Path
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

class KryptonWorld(
    override val server: KryptonServer,
    val data: WorldData,
    override val dimension: ResourceKey<World>,
    override val dimensionType: KryptonDimensionType,
    override val seed: Long,
    private val tickTime: Boolean
) : World, WorldAccessor, PacketGroupingAudience {

    override val biomeManager: BiomeManager = BiomeManager(this, seed)
    private val random: Random = Random()
    override val border: KryptonWorldBorder = KryptonWorldBorder.DEFAULT // FIXME
    override val gameMode: GameMode
        get() = data.gameMode
    override var difficulty: Difficulty
        get() = data.difficulty
        set(value) {
            data.difficulty = value
            cachedDifficultyPacket.invalidate()
        }
    val cachedDifficultyPacket: CachedPacket = CachedPacket { PacketOutChangeDifficulty(difficulty) }
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
    override val folder: Path = data.folder
    override val scoreboard: KryptonScoreboard = server.scoreboard

    override val chunks: Collection<KryptonChunk>
        get() = chunkManager.chunkMap.values

    val entityManager: EntityManager = EntityManager(this)
    override val entities: MutableCollection<KryptonEntity> = entityManager.entities

    override val chunkManager: ChunkManager = ChunkManager(this)
    private val playerManager: PlayerManager = server.playerManager
    override val sessionManager: SessionManager = server.sessionManager
    override val players: MutableSet<KryptonPlayer> = ConcurrentHashMap.newKeySet()

    override val height: Int = dimensionType.height
    override val minimumBuildHeight: Int = dimensionType.minimumY

    private var oldRainLevel = 0F
    override var rainLevel: Float = 0F
        set(value) {
            val clamped = value.clamp(0F, 1F)
            oldRainLevel = clamped
            field = clamped
        }
    private var oldThunderLevel = 0F
    override var thunderLevel: Float = 0F
        set(value) {
            val clamped = value.clamp(0F, 1F)
            oldRainLevel = clamped
            field = clamped
        }

    private var cachedPointers: Pointers? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vector3d): T? {
        if (!type.isSummonable || type === EntityTypes.PLAYER) return null
        val entity = EntityFactory.create(type, this)?.apply { this.location = location } ?: return null
        spawnEntity(entity)
        return entity as? T
    }

    fun spawnEntity(entity: KryptonEntity) {
        entityManager.spawn(entity)
    }

    fun spawnPlayer(player: KryptonPlayer) {
        entityManager.spawn(player)
    }

    fun removeEntity(entity: KryptonEntity) {
        entityManager.remove(entity)
    }

    fun worldEvent(event: WorldEvent, x: Int, y: Int, z: Int, data: Int, except: KryptonPlayer) {
        playerManager.broadcast(PacketOutWorldEvent(event, x, y, z, data, false), this, x, y, z, 64.0, except)
    }

    fun playSound(position: Vector3d, event: SoundEvent, source: Sound.Source, volume: Float, pitch: Float, except: KryptonPlayer? = null) {
        playSound(position.x(), position.y(), position.z(), event, source, volume, pitch, except)
    }

    fun playSound(
        x: Double,
        y: Double,
        z: Double,
        event: SoundEvent,
        source: Sound.Source,
        volume: Float,
        pitch: Float,
        except: KryptonPlayer? = null
    ) {
        playerManager.broadcast(PacketOutSoundEffect(event, source, x, y, z, volume, pitch), this, x, y, z, event.calculateDistance(volume), except)
    }

    // TODO: Check world border bounds
    fun canInteract(player: KryptonPlayer, x: Int, z: Int): Boolean = !server.isProtected(this, x, z, player)

    fun broadcastBlockDestroyProgress(sourceId: Int, x: Int, y: Int, z: Int, state: Int) {
        val packet = PacketOutSetBlockDestroyStage(sourceId, x, y, z, state)
        sessionManager.sendGrouped(packet) {
            if (it.world !== this || it.id == sourceId) return@sendGrouped false
            val distanceX = x - it.location.x()
            val distanceY = y - it.location.y()
            val distanceZ = z - it.location.z()
            distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ < 1024.0
        }
    }

    fun removeBlock(x: Int, y: Int, z: Int): Boolean {
        val fluid = getFluid(x, y, z)
        return setBlock(x, y, z, fluid.asBlock())
    }

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState {
        if (isOutsideBuildHeight(y)) return Blocks.VOID_AIR.defaultState.downcast()
        val chunk = getChunkAt(x shr 4, z shr 4) ?: return Blocks.AIR.defaultState.downcast()
        return chunk.getBlock(x, y, z)
    }

    override fun getBlock(position: Vector3i): KryptonBlockState = getBlock(position.x(), position.y(), position.z())

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState {
        if (isOutsideBuildHeight(y)) return KryptonFluids.EMPTY.defaultState
        val chunk = getChunkAt(x shr 4, z shr 4) ?: return KryptonFluids.EMPTY.defaultState
        return chunk.getFluid(x, y, z)
    }

    override fun getFluid(position: Vector3i): KryptonFluidState = getFluid(position.x(), position.y(), position.z())

    override fun getChunk(x: Int, z: Int, status: ChunkStatus, shouldCreate: Boolean): ChunkAccessor? = null // FIXME

    override fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int {
        if (x in MINIMUM_SIZE..MAXIMUM_SIZE && z in MINIMUM_SIZE..MAXIMUM_SIZE) {
            if (hasChunk(x shr 4, z shr 4)) return getChunk(x shr 4, z shr 4)!!.getHeight(type, x and 15, z and 15) + 1
            return minimumBuildHeight
        }
        return SEA_LEVEL + 1
    }

    override fun getChunkAt(x: Int, z: Int): KryptonChunk? = chunkManager[x, z]

    override fun getChunk(x: Int, y: Int, z: Int): KryptonChunk? = getChunkAt(x shr 4, z shr 4)

    override fun getChunk(position: Vector3i): KryptonChunk? = getChunk(position.x(), position.y(), position.z())

    override fun loadChunk(x: Int, z: Int): KryptonChunk? = chunkManager.load(x, z, Ticket(TicketTypes.API_LOAD, 31, ChunkPosition.toLong(x, z)))

    override fun unloadChunk(x: Int, z: Int, force: Boolean) {
        chunkManager.unload(x, z, TicketTypes.API_LOAD, force)
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: KryptonBlockState): Boolean {
        if (isOutsideBuildHeight(y)) return false
//        if (isDebug) return false
        val chunk = getChunk(x, y, z) ?: return false
        if (!chunk.setBlock(x, y, z, block)) return false
        sessionManager.sendGrouped(PacketOutBlockUpdate(block, x, y, z))
        return true
    }

    fun tick() {
        if (players.isEmpty()) return // don't tick the world if there's no players in it

        // tick rain
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

        if (oldRainLevel != rainLevel) {
            sessionManager.sendGrouped(PacketOutGameEvent(GameEvent.RAIN_LEVEL_CHANGE, rainLevel)) { it.world === this }
        }
        if (oldThunderLevel != thunderLevel) {
            sessionManager.sendGrouped(PacketOutGameEvent(GameEvent.THUNDER_LEVEL_CHANGE, thunderLevel)) { it.world === this }
        }
        if (wasRaining != isRaining) {
            val newRainState = if (wasRaining) GameEvent.END_RAINING else GameEvent.BEGIN_RAINING
            sessionManager.sendGrouped(PacketOutGameEvent(newRainState)) { it.world === this }
            sessionManager.sendGrouped(PacketOutGameEvent(GameEvent.RAIN_LEVEL_CHANGE, rainLevel)) { it.world === this }
            sessionManager.sendGrouped(PacketOutGameEvent(GameEvent.THUNDER_LEVEL_CHANGE, thunderLevel)) { it.world === this }
        }

        tickTime()
        chunkManager.chunkMap.values.forEach { it.tick(chunkManager.players(it.position.toLong()).size) }

        if (players.isNotEmpty()) entities.forEach {
            if (it.isRemoved) return@forEach
            it.tick()
        }
    }

    private fun tickTime() {
        if (!tickTime) return
        data.time++
        if (data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]) data.dayTime++
    }

    fun save(shouldClose: Boolean) {
        chunkManager.saveAll(shouldClose)
        if (shouldClose) entityManager.close() else entityManager.flush()
    }

    fun getRainLevel(delta: Float): Float = GenericMath.lerp(oldRainLevel, rainLevel, delta)

    fun getThunderLevel(delta: Float): Float = GenericMath.lerp(oldThunderLevel, thunderLevel, delta) * getRainLevel(delta)

    override fun pointers(): Pointers {
        if (cachedPointers == null) cachedPointers = Pointers.builder().withDynamic(Identity.NAME, ::name).build()
        return cachedPointers!!
    }

    companion object {

        private const val MINIMUM_SIZE = -30000000
        private const val MAXIMUM_SIZE = -MINIMUM_SIZE
        private const val SEA_LEVEL = 63
    }
}
