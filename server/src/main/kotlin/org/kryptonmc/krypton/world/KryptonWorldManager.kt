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

import com.google.common.hash.Hashing
import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.registry.ops.RegistryReadOps
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.progress.ChunkProgressListener
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.data.DerivedWorldData
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldResource
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.dimension.storageFolder
import org.kryptonmc.krypton.world.storage.WorldDataStorage
import org.kryptonmc.nbt.Tag
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.io.path.exists

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(
    override val server: KryptonServer,
    private val worldData: PrimaryWorldData,
    private val ops: RegistryReadOps<Tag>,
    val worldFolder: Path
) : WorldManager {

    private val worldExecutor = Executors.newCachedThreadPool(NamedThreadFactory("World Handler %d"))
    private val customWorldFolder = worldFolder.resolve("dimensions")
    val worlds = mutableMapOf<ResourceKey<World>, KryptonWorld>()
    override val default: KryptonWorld
        get() = worlds[World.OVERWORLD] ?: error("The default world has not yet been loaded!")

    init {
        val name = server.config.world.name
        Messages.WORLD.LOADED.info(LOGGER)
    }

    fun loadWorlds() {
        val progressListener = ChunkProgressListener(11)
        createWorlds(progressListener)
        prepareWorlds(ChunkProgressListener(11))
    }

    fun <T> readData(folder: Path, reader: (Path) -> T): T? {
        if (!folder.exists()) return null
        var levelFile = folder.resolve(WorldResource.LEVEL_DATA_FILE.path)
        if (levelFile.exists()) reader(levelFile)?.let { return it }
        levelFile = folder.resolve("${WorldResource.LEVEL_DATA_FILE.path}_old")
        return if (levelFile.exists()) reader(levelFile) else null
    }

    override fun get(key: Key) = worlds[ResourceKey.of(ResourceKeys.DIMENSION, key)]

    override fun load(key: Key): CompletableFuture<KryptonWorld> {
        val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key)
        if (resourceKey === World.OVERWORLD) return CompletableFuture.failedFuture(IllegalArgumentException("The default world cannot be loaded!"))
        val loaded = worlds[resourceKey]
        if (loaded != null) return CompletableFuture.completedFuture(loaded)
        val dimensionType = server.registryHolder.registryOrThrow(ResourceKeys.DIMENSION_TYPE)[key]
            ?: return CompletableFuture.failedFuture(IllegalStateException("No dimension type found for given key $key!"))
        val defaultData = server.worldData
        Messages.WORLD.LOAD.info(LOGGER, key.asString())
        val folderName = key.storageFolder
        val isSubWorld = folderName == "DIM-1" || folderName == "DIM1"
        val storage = try {
            WorldDataStorage(if (isSubWorld) worldFolder else customWorldFolder).createAccess(if (isSubWorld) folderName else key.namespace() + File.separator + key.value())
        } catch (exception: IOException) {
            return CompletableFuture.failedFuture(RuntimeException("Failed to create world data for world $key!", exception))
        }
        return CompletableFuture.supplyAsync({
            val worldData = storage.loadData(ops, defaultData.dataPackConfig) ?: kotlin.run {
                val gamemode = server.config.world.gamemode
                val difficulty = server.config.world.difficulty
                val hardcore = server.config.world.hardcore
                val rules = KryptonGameRuleHolder()
                PrimaryWorldData(folderName, gamemode, difficulty, hardcore, rules, defaultData.dataPackConfig, defaultData.worldGenerationSettings)
            }
            worldData.setModdedInfo(KryptonServerInfo.name, true)
            val isDebug = worldData.worldGenerationSettings.isDebug
            val seed = Hashing.sha256().hashLong(worldData.worldGenerationSettings.seed).asLong()
            KryptonWorld(server, storage, worldData, resourceKey, dimensionType, ChunkProgressListener(11), isDebug, seed, true)
        }, worldExecutor)
    }

    override fun save(world: World): CompletableFuture<Unit> = CompletableFuture.supplyAsync({
        world.save()
        if (world is KryptonWorld) world.chunkCache.save()
    }, worldExecutor)

    override fun contains(key: Key) = worlds.containsKey(ResourceKey.of(ResourceKeys.DIMENSION, key))

    private fun createWorlds(progressListener: ChunkProgressListener) {
        val generationSettings = worldData.worldGenerationSettings
        val isDebug = generationSettings.isDebug
        val seed = Hashing.sha256().hashLong(generationSettings.seed).asLong()
        val dimensions = generationSettings.dimensions
        val overworld = dimensions[Dimension.OVERWORLD]
        val dimensionType = overworld?.type ?: server.registryHolder.registryOrThrow(ResourceKeys.DIMENSION_TYPE)[DimensionTypes.OVERWORLD_KEY]!!
        val world = KryptonWorld(server, server.storageSource, worldData, World.OVERWORLD, dimensionType, progressListener, isDebug, seed, true)
        worlds[World.OVERWORLD] = world
        if (!worldData.isInitialized) {
            world.setInitialSpawn(worldData, isDebug)
            worldData.isInitialized = true
            if (isDebug) setupDebugWorld(worldData)
        }
        dimensions.entries.forEach { (key, dimension) ->
            if (key === Dimension.OVERWORLD) return@forEach
            val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key.location)
            val type = dimension.type
            val data = DerivedWorldData(worldData)
            worlds[resourceKey] = KryptonWorld(server, server.storageSource, data, resourceKey, type, progressListener, isDebug, seed, false)
        }
        // TODO: Update mob spawning flags
    }

    private fun prepareWorlds(progressListener: ChunkProgressListener) {
        val overworld = worlds[World.OVERWORLD] ?: error("The overworld was not loaded! But... this is impossible... :thonking:")
        LOGGER.info("Preparing start region for dimension ${overworld.dimension.location}...")
        overworld.chunkCache.init()
        overworld.chunkCache.addRegionTicket(TicketTypes.START, ChunkPosition(overworld.data.spawnX shr 4, overworld.data.spawnZ shr 4), 11, Unit)
    }

    private fun setupDebugWorld(data: PrimaryWorldData) {
        data.difficulty = Difficulty.PEACEFUL
        data.isRaining = false
        data.isThundering = false
        data.clearWeatherTime = 1000000000
        data.dayTime = 6000L
        data.gamemode = Gamemode.SPECTATOR
    }

    fun saveAll() = worlds.forEach { (_, world) -> save(world).get() }

    companion object {

        private const val DEFAULT_BUILD_LIMIT = 256
        private val OLD_WORLD_GEN_SETTINGS_KEYS = listOf(
            "RandomSeed",
            "generatorName",
            "generatorOptions",
            "generatorVersion",
            "legacy_custom_options",
            "MapFeatures",
            "BonusChest"
        )
        private val LOGGER = logger<KryptonWorldManager>()
    }
}

fun <K, V, K1, V1> Map<K, V>.transform(function: (Map.Entry<K, V>) -> Pair<K1, V1>): Map<K1, V1> {
    val temp = mutableMapOf<K1, V1>()
    for (entry in this) {
        temp += function(entry)
    }
    return temp
}
