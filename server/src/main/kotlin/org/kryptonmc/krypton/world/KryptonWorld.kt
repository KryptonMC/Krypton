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
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.effect.sound.downcast
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.EntityManager
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameEventTypes
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetBlockDestroyStage
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutWorldEvent
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.SectionPos
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.SetBlockFlag
import org.kryptonmc.krypton.world.components.BaseWorld
import org.kryptonmc.krypton.world.data.WorldData
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.flag.FeatureFlagSet
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import org.kryptonmc.krypton.world.redstone.BatchingNeighbourUpdater
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import org.kryptonmc.krypton.world.rule.WorldGameRules
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BooleanSupplier

class KryptonWorld(
    override val server: KryptonServer,
    override val data: WorldData,
    override val dimension: ResourceKey<World>,
    override val dimensionType: KryptonDimensionType,
    override val seed: Long,
    private val tickTime: Boolean
) : BaseWorld, AutoCloseable {

    override val chunkManager: ChunkManager = ChunkManager(this)
    override val entityManager: EntityManager = EntityManager(this)
    override val biomeManager: BiomeManager = BiomeManager(this, seed)
    override val random: RandomSource = RandomSource.create()
    private val threadSafeRandom: RandomSource = RandomSource.createThreadSafe()
    override val border: KryptonWorldBorder = KryptonWorldBorder.DEFAULT // FIXME
    override var difficulty: Difficulty
        get() = data.difficulty
        set(value) {
            data.difficulty = value
        }
    override val isRaining: Boolean
        get() = getRainLevel(1F) > 0.2F
    override val isThundering: Boolean
        get() = if (dimensionType.hasSkylight && !dimensionType.hasCeiling) getThunderLevel(1F) > 0.9F else false
    private var skyDarken = 0

    override val players: MutableSet<KryptonPlayer> = ConcurrentHashMap.newKeySet()
    var doNotSave: Boolean = false

    private var oldRainLevel = 0F
    override var rainLevel: Float = 0F
        set(value) {
            val clamped = Maths.clamp(value, 0F, 1F)
            oldRainLevel = clamped
            field = clamped
        }
    private var oldThunderLevel = 0F
    override var thunderLevel: Float = 0F
        set(value) {
            val clamped = Maths.clamp(value, 0F, 1F)
            oldRainLevel = clamped
            field = clamped
        }

    private val neighbourUpdater = BatchingNeighbourUpdater(this, server.config.advanced.maximumChainedNeighbourUpdates)
    private var cachedPointers: Pointers? = null

    init {
        updateSkyBrightness()
        prepareWeather()
    }

    fun gameRules(): WorldGameRules = data.gameRules

    @Suppress("UNCHECKED_CAST")
    override fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vec3d): T? {
        if (!type.isSummonable || type === EntityTypes.PLAYER) return null
        val entity = EntityFactory.create(type, this)?.apply { this.position = location } ?: return null
        spawnEntity(entity)
        return entity as? T
    }

    fun spawnEntity(entity: KryptonEntity) {
        entityManager.spawnEntity(entity)
    }

    fun spawnPlayer(player: KryptonPlayer) {
        entityManager.spawnPlayer(player)
    }

    fun removeEntity(entity: KryptonEntity) {
        entityManager.removeEntity(entity)
    }

    override fun worldEvent(pos: BlockPos, event: Int, data: Int, except: KryptonPlayer?) {
        server.playerManager.broadcast(PacketOutWorldEvent(event, pos, data, false), this, pos, 64.0, except)
    }

    override fun playSound(pos: BlockPos, event: SoundEvent, source: Sound.Source, volume: Float, pitch: Float, except: KryptonPlayer?) {
        playSound(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, event, source, volume, pitch, except)
    }

    fun playSound(entity: KryptonEntity, event: SoundEvent, source: Sound.Source, volume: Float, pitch: Float, except: KryptonPlayer? = null) {
        playSeededSound(entity, KryptonRegistries.SOUND_EVENT.wrapAsHolder(event), source, volume, pitch, threadSafeRandom.nextLong(), except)
    }

    fun playSound(x: Double, y: Double, z: Double, event: SoundEvent, source: Sound.Source, volume: Float, pitch: Float,
                  except: KryptonPlayer? = null) {
        playSeededSound(x, y, z, KryptonRegistries.SOUND_EVENT.wrapAsHolder(event), source, volume, pitch, threadSafeRandom.nextLong(), except)
    }

    private fun playSeededSound(x: Double, y: Double, z: Double, event: Holder<SoundEvent>, source: Sound.Source, volume: Float, pitch: Float,
                                seed: Long, except: KryptonPlayer?) {
        val packet = PacketOutSoundEffect.create(event, source, x, y, z, volume, pitch, seed)
        server.playerManager.broadcast(packet, this, x, y, z, event.value().downcast().getRange(volume).toDouble(), except)
    }

    private fun playSeededSound(entity: KryptonEntity, event: Holder<SoundEvent>, source: Sound.Source, volume: Float, pitch: Float, seed: Long,
                                except: KryptonPlayer?) {
        val packet = PacketOutEntitySoundEffect(event, source, entity.id, volume, pitch, seed)
        val range = event.value().downcast().getRange(volume).toDouble()
        server.playerManager.broadcast(packet, this, entity.position.x, entity.position.y, entity.position.z, range, except)
    }

    // TODO: Check world border bounds
    fun canInteract(player: KryptonPlayer, x: Int, z: Int): Boolean = !server.isProtected(this, x, z, player)

    fun broadcastBlockDestroyProgress(sourceId: Int, position: BlockPos, state: Int) {
        val packet = PacketOutSetBlockDestroyStage(sourceId, position, state)
        server.sessionManager.sendGrouped(packet) {
            if (it.world !== this || it.id == sourceId) return@sendGrouped false
            val dx = position.x - it.position.x
            val dy = position.y - it.position.y
            val dz = position.z - it.position.z
            dx * dx + dy * dy + dz * dz < 32.0 * 32.0
        }
    }

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState {
        if (isOutsideBuildHeight(y)) return KryptonBlocks.VOID_AIR.defaultState
        return getChunkAt(SectionPos.blockToSection(x), SectionPos.blockToSection(z))?.getBlock(x, y, z) ?: KryptonBlocks.AIR.defaultState
    }

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState {
        if (isOutsideBuildHeight(y)) return KryptonFluids.EMPTY.defaultState
        return getChunkAt(SectionPos.blockToSection(x), SectionPos.blockToSection(z))?.getFluid(x, y, z) ?: KryptonFluids.EMPTY.defaultState
    }

    override fun getChunk(x: Int, z: Int, requiredStatus: ChunkStatus, shouldCreate: Boolean): ChunkAccessor? = null // FIXME

    override fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int {
        if (x in MINIMUM_SIZE..MAXIMUM_SIZE && z in MINIMUM_SIZE..MAXIMUM_SIZE) {
            val chunkX = SectionPos.sectionToBlock(x)
            val chunkZ = SectionPos.sectionRelative(z)
            val sectionX = SectionPos.sectionRelative(x)
            val sectionZ = SectionPos.sectionRelative(z)
            if (hasChunk(chunkX, chunkZ)) return getChunk(chunkX, chunkZ)!!.getHeight(type, sectionX, sectionZ) + 1
            return minimumBuildHeight()
        }
        return seaLevel() + 1
    }

    override fun setBlock(pos: BlockPos, state: KryptonBlockState, flags: Int, recursionLeft: Int): Boolean {
        if (isOutsideBuildHeight(pos)) return false
//        if (isDebug) return false
        val chunk = getChunk(pos) ?: return false
        val block = state.block
        val oldState = chunk.setBlock(pos, state, flags and SetBlockFlag.BLOCK_MOVING != 0) ?: return false
        val newState = getBlock(pos)
        if (flags and SetBlockFlag.LIGHTING == 0 && newState !== oldState && canUpdateLighting(pos, oldState, newState)) {
            // TODO: Update lighting for block
        }
        if (newState === state) {
            if (flags and SetBlockFlag.NOTIFY_CLIENTS != 0) sendBlockUpdated(pos, oldState, newState)
            if (flags and SetBlockFlag.UPDATE_NEIGHBOURS != 0) {
                blockUpdated(pos, oldState.block)
                if (state.hasAnalogOutputSignal()) updateNeighbourForOutputSignal(pos, block)
            }
            if (flags and SetBlockFlag.UPDATE_NEIGHBOUR_SHAPES == 0 && recursionLeft > 0) {
                val maskedFlags = flags and (SetBlockFlag.NEIGHBOUR_DROPS.inv() and SetBlockFlag.UPDATE_NEIGHBOURS.inv())
                oldState.updateIndirectNeighbourShapes(this, pos, maskedFlags, recursionLeft - 1)
                state.updateNeighbourShapes(this, pos, maskedFlags, recursionLeft - 1)
                state.updateIndirectNeighbourShapes(this, pos, maskedFlags, recursionLeft - 1)
            }
            onBlockStateChange(pos, oldState, newState)
        }
        return true
    }

    private fun canUpdateLighting(pos: BlockPos, oldState: KryptonBlockState, newState: KryptonBlockState): Boolean =
        newState.getLightBlock(this, pos) != oldState.getLightBlock(this, pos) ||
                newState.lightEmission != oldState.lightEmission ||
                newState.useShapeForLightOcclusion != oldState.useShapeForLightOcclusion

    @Suppress("UnusedPrivateMember")
    fun sendBlockUpdated(pos: BlockPos, oldState: KryptonBlockState, newState: KryptonBlockState) {
        server.sessionManager.sendGrouped(players, PacketOutBlockUpdate(pos, newState))
        // TODO: Update pathfinding mobs
    }

    private fun updateNeighbourForOutputSignal(pos: BlockPos, block: KryptonBlock) {
        Directions.Plane.HORIZONTAL.forEach { direction ->
            var neighbourPos = pos.relative(direction)
            if (!hasChunkAt(neighbourPos)) return@forEach
            var neighbour = getBlock(neighbourPos)
            if (neighbour.eq(KryptonBlocks.COMPARATOR)) {
                neighbourChanged(neighbour, neighbourPos, block, pos, false)
            } else if (neighbour.isRedstoneConductor(this, neighbourPos)) {
                neighbourPos = neighbourPos.relative(direction)
                neighbour = getBlock(neighbourPos)
                if (neighbour.eq(KryptonBlocks.COMPARATOR)) neighbourChanged(neighbour, neighbourPos, block, pos, false)
            }
        }
    }

    private fun updateNeighboursAt(pos: BlockPos, block: KryptonBlock) {
        neighbourUpdater.updateNeighboursAtExceptFromFacing(pos, block, null)
    }

    private fun neighbourChanged(state: KryptonBlockState, pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos, moving: Boolean) {
        neighbourUpdater.neighbourChanged(state, pos, block, neighbourPos, moving)
    }

    @Suppress("UnusedPrivateMember")
    private fun onBlockStateChange(pos: BlockPos, oldState: KryptonBlockState, newState: KryptonBlockState) {
        // TODO: Do POI updates
    }

    override fun blockUpdated(pos: BlockPos, block: KryptonBlock) {
        updateNeighboursAt(pos, block)
    }

    override fun removeBlock(pos: BlockPos, moving: Boolean): Boolean =
        setBlock(pos, getFluid(pos).asBlock(), SetBlockFlag.UPDATE_NOTIFY or if (moving) SetBlockFlag.BLOCK_MOVING else 0)

    override fun destroyBlock(pos: BlockPos, drop: Boolean, entity: KryptonEntity?, recursionLeft: Int): Boolean {
        val state = getBlock(pos)
        if (state.isAir) return false
        val fluid = getFluid(pos)
//        if (state.block !is BaseFireBlock) worldEvent(pos, WorldEvent.DESTROY_BLOCK, KryptonBlock.idOf(state)) TODO
        if (drop) {
            // TODO: Drop items
        }
        return setBlock(pos, fluid.asBlock(), SetBlockFlag.UPDATE_NOTIFY, recursionLeft)
    }

    override fun getUncachedNoiseBiome(x: Int, y: Int, z: Int): Biome = Biomes.PLAINS.get() // TODO: Use biome source from chunk generator

    fun tick(hasTimeLeft: BooleanSupplier) {
        tickWeather()
        updateSkyBrightness()
        tickTime()
        chunkManager.tick(hasTimeLeft)

        if (players.isNotEmpty()) {
            entities.forEach { if (!it.isRemoved) it.tick() }
        }
    }

    private fun tickWeather() {
        val wasRaining = isRaining
        if (dimensionType.hasSkylight) {
            if (gameRules().getBoolean(GameRuleKeys.DO_WEATHER_CYCLE)) {
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
            thunderLevel = Maths.clamp(thunderLevel, 0F, 1F)
            oldRainLevel = rainLevel
            rainLevel = if (data.isRaining) rainLevel + 0.01F else rainLevel - 0.01F
            rainLevel = Maths.clamp(rainLevel, 0F, 1F)
        }

        if (oldRainLevel != rainLevel) {
            server.sessionManager.sendGrouped(PacketOutGameEvent(GameEventTypes.RAIN_LEVEL_CHANGE, rainLevel)) { it.world === this }
        }
        if (oldThunderLevel != thunderLevel) {
            server.sessionManager.sendGrouped(PacketOutGameEvent(GameEventTypes.THUNDER_LEVEL_CHANGE, thunderLevel)) { it.world === this }
        }
        if (wasRaining != isRaining) {
            val newRainState = if (wasRaining) GameEventTypes.END_RAINING else GameEventTypes.BEGIN_RAINING
            server.sessionManager.sendGrouped(PacketOutGameEvent(newRainState)) { it.world === this }
            server.sessionManager.sendGrouped(PacketOutGameEvent(GameEventTypes.RAIN_LEVEL_CHANGE, rainLevel)) { it.world === this }
            server.sessionManager.sendGrouped(PacketOutGameEvent(GameEventTypes.THUNDER_LEVEL_CHANGE, thunderLevel)) { it.world === this }
        }
    }

    private fun tickTime() {
        if (!tickTime) return
        data.time++
        if (gameRules().getBoolean(GameRuleKeys.DO_DAYLIGHT_CYCLE)) data.dayTime++
    }

    private fun updateSkyBrightness() {
        val rainFactor = 1.0 - (getRainLevel(1F) * 5F).toDouble() / 16.0
        val thunderFactor = 1.0 - (getThunderLevel(1F) * 5F).toDouble() / 16.0
        val result = 0.5 + 2.0 * Maths.clamp(Maths.cos(timeOfDay(1F) * (Math.PI.toFloat() * 2F)).toDouble(), -0.25, 0.25)
        skyDarken = ((1.0 - result * rainFactor * thunderFactor) * 11.0).toInt()
    }

    private fun prepareWeather() {
        if (data.isRaining) {
            rainLevel = 1F
            if (data.isThundering) thunderLevel = 1F
        }
    }

    override fun generateSoundSeed(): Long = threadSafeRandom.nextLong()

    fun save(flush: Boolean, skipSave: Boolean) {
        if (skipSave) return
        // TODO: Save extra data for maps, raids, etc.
        chunkManager.saveAllChunks(flush)
        if (flush) entityManager.flush()
    }

    override fun close() {
        chunkManager.close()
        entityManager.close()
    }

    fun getRainLevel(delta: Float): Float = Maths.lerp(delta, oldRainLevel, rainLevel)

    fun getThunderLevel(delta: Float): Float = Maths.lerp(delta, oldThunderLevel, thunderLevel) * getRainLevel(delta)

    override fun pointers(): Pointers {
        if (cachedPointers == null) cachedPointers = Pointers.builder().withDynamic(Identity.NAME, ::name).build()
        return cachedPointers!!
    }

    override fun sendGroupedPacket(players: Collection<KryptonPlayer>, packet: Packet) {
        server.sessionManager.sendGrouped(players, packet)
    }

    override fun skyDarken(): Int = skyDarken

    override fun seaLevel(): Int = 63

    override fun enabledFeatures(): FeatureFlagSet = data.enabledFeatures()

    override fun toString(): String = "KryptonWorld[${data.name}]"

    companion object {

        private const val MINIMUM_SIZE = -30000000
        private const val MAXIMUM_SIZE = -MINIMUM_SIZE
    }
}
