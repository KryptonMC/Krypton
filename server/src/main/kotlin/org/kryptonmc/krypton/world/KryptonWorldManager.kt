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
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.util.ChunkProgressListener
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.data.DerivedWorldData
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldResource
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.dimension.storageFolder
import org.kryptonmc.krypton.world.generation.DebugGenerator
import org.kryptonmc.krypton.world.generation.FlatGenerator
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.generation.flat.FlatGeneratorSettings
import org.kryptonmc.krypton.world.storage.WorldDataAccess
import org.kryptonmc.krypton.world.storage.WorldDataStorage
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.util.Random
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.io.path.exists

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(
    override val server: KryptonServer,
    private val dataAccess: WorldDataAccess,
    private val worldData: PrimaryWorldData,
    val worldFolder: Path
) : WorldManager {

    private val worldExecutor = Executors.newCachedThreadPool(NamedThreadFactory("World Handler %d"))
    private val customWorldFolder = worldFolder.resolve("dimensions")
    override val worlds = mutableMapOf<ResourceKey<World>, KryptonWorld>()
    override val default: KryptonWorld
        get() = worlds[World.OVERWORLD] ?: error("The default world has not yet been loaded!")

    fun init() {
        create()
        prepare()
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
        val dimensionType = DimensionTypes.OVERWORLD
//            ?: return CompletableFuture.failedFuture(IllegalStateException("No dimension type found for given key $key!"))
        val generator = server.worldData.worldGenerationSettings.dimensions[ResourceKey.of(InternalResourceKeys.DIMENSION, key)]?.generator
            ?: return CompletableFuture.failedFuture(IllegalStateException("No generator found for given key $key!"))
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
            val worldData = storage.loadData(NBTOps, defaultData.dataPackConfig) ?: kotlin.run {
                val gamemode = server.config.world.gamemode
                val difficulty = server.config.world.difficulty
                val hardcore = server.config.world.hardcore
                val rules = KryptonGameRuleHolder()
                PrimaryWorldData(folderName, gamemode, difficulty, hardcore, rules, defaultData.dataPackConfig, defaultData.worldGenerationSettings)
            }
            worldData.setModdedInfo(KryptonPlatform.name, true)
            val isDebug = worldData.worldGenerationSettings.isDebug
            val seed = Hashing.sha256().hashLong(worldData.worldGenerationSettings.seed).asLong()
            KryptonWorld(server, storage, worldData, resourceKey, dimensionType, generator, isDebug, seed, true)
        }, worldExecutor)
    }

    override fun save(world: World): CompletableFuture<Unit> = CompletableFuture.supplyAsync({ world.save() }, worldExecutor)

    override fun contains(key: Key) = worlds.containsKey(ResourceKey.of(ResourceKeys.DIMENSION, key))

    private fun create() {
        val generationSettings = worldData.worldGenerationSettings
        val isDebug = generationSettings.isDebug
        val seed = Hashing.sha256().hashLong(generationSettings.seed).asLong()
        val dimensions = generationSettings.dimensions
        val overworld = dimensions[Dimension.OVERWORLD]
        val dimensionType = overworld?.type ?: DimensionTypes.OVERWORLD
        val generator = overworld?.generator ?: DebugGenerator(InternalRegistries.BIOME)
        val world = KryptonWorld(server, server.dataAccess, worldData, World.OVERWORLD, dimensionType, generator, isDebug, seed, true)
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
            worlds[resourceKey] = KryptonWorld(server, server.dataAccess, data, resourceKey, type, dimension.generator, isDebug, seed, false)
        }
    }

    private fun prepare() {
        val listener = ChunkProgressListener(9)
        LOGGER.info("Preparing start region for dimension ${default.dimension.location}...")
        listener.tick()
        default.chunkManager.addTicket(default.data.spawnX shr 4, default.data.spawnZ shr 4, TicketTypes.START, 22, Unit) {
            listener.updateStatus(ChunkStatus.FULL)
        }
        listener.stop()
    }

    private fun setupDebugWorld(data: PrimaryWorldData) {
        data.difficulty = Difficulty.PEACEFUL
        data.isRaining = false
        data.isThundering = false
        data.clearWeatherTime = 1000000000
        data.dayTime = 6000L
        data.gamemode = Gamemode.SPECTATOR
    }

    fun saveAll(): Boolean {
        var successful = false
        worlds.values.forEach {
            it.save()
            successful = true
        }
        dataAccess.saveData(worldData)
        return successful
    }

    companion object {

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
