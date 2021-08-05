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
package org.kryptonmc.krypton.world.storage

import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.orThrow
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.registry.RegistryLookupCodec
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.lock
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldResource
import org.kryptonmc.krypton.world.data.parseWorldData
import org.kryptonmc.krypton.world.dimension.storageFolder
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Path

class WorldDataAccess(
    private val storage: WorldDataStorage,
    val id: String,
) : AutoCloseable {

    val path: Path = storage.baseFolder.resolve(id)
    private val lock = path.lock()
    private val resources = mutableMapOf<WorldResource, Path>()

    val dataPacks: DataPackConfig?
        get() {
            checkLock()
            return storage.loadData(path, Path::dataPacks)
        }

    fun resourcePath(resource: WorldResource) = resources.getOrPut(resource) { path.resolve(resource.path) }

    fun dimensionPath(key: ResourceKey<World>) = key.storageFolder(path)

    fun loadData(ops: DynamicOps<Tag>, dataPackConfig: DataPackConfig): PrimaryWorldData? {
        checkLock()
        return storage.loadData(path, getWorldData(ops, dataPackConfig))
    }

    fun saveData(registryHolder: RegistryHolder, data: PrimaryWorldData) = storage.saveData(registryHolder, path, data)

    override fun close() = lock.close()

    private fun checkLock() {
        check(lock.isValid) { "Lock is no longer valid!" }
    }
}

private val LOGGER = logger<WorldDataAccess>()
private val OLD_SETTINGS_KEYS = setOf(
    "RandomSeed",
    "generatorName",
    "generatorOptions",
    "generatorVersion",
    "legacy_custom_options",
    "MapFeatures",
    "BonusChest"
)

private val Path.dataPacks: DataPackConfig?
    get() = try {
        val tag = TagIO.read(this, TagCompression.GZIP).getCompound("Data")
        val version = if (tag.contains("DataVersion", 99)) tag.getInt("DataVersion") else -1
        val data = DATA_FIXER.update(References.LEVEL, Dynamic(NBTOps, tag), version, KryptonPlatform.worldVersion)
        data["DataPacks"].result().map { it.toDataPackConfig() }.orElse(DataPackConfig.DEFAULT)
    } catch (exception: Exception) {
        LOGGER.error("Caught exception whilst trying to read $this!", exception)
        null
    }

private fun getWorldData(ops: DynamicOps<Tag>, dataPackConfig: DataPackConfig): (Path) -> PrimaryWorldData? = {
    try {
        val tag = TagIO.read(it, TagCompression.GZIP).getCompound("Data")
        val version = if (tag.contains("DataVersion", 99)) tag.getInt("DataVersion") else -1
        val data = DATA_FIXER.update(References.LEVEL, Dynamic(ops, tag), version, KryptonPlatform.worldVersion)
        val worldGenSettings = data.readWorldGenSettings(version)
        data.parseWorldData(worldGenSettings, dataPackConfig)
    } catch (exception: Exception) {
        LOGGER.error("Caught exception whilst trying to read $it!", exception)
        null
    }
}

private fun <T> Dynamic<T>.readWorldGenSettings(version: Int): WorldGenerationSettings {
    var temp = get("WorldGenSettings").orElseEmptyMap()
    OLD_SETTINGS_KEYS.forEach { key -> get(key).result().ifPresent { temp = temp.set(key, it) } }
    val data = DATA_FIXER.update(References.WORLD_GEN_SETTINGS, temp, version, KryptonPlatform.worldVersion)
    return WorldGenerationSettings.CODEC.parse(data).resultOrPartial { LOGGER.error("WorldGenSettings: $it") }.orElseGet {
        val dimensionTypes = RegistryLookupCodec(InternalResourceKeys.DIMENSION_TYPE).codec().parse(data).resultOrPartial { LOGGER.error("Dimension type registry: $it") }.orThrow("Failed to get dimension registry!")
        val biomes = RegistryLookupCodec(InternalResourceKeys.BIOME).codec().parse(data).resultOrPartial { LOGGER.error("Biome registry: $it") }.orThrow("Failed to get biome registry")
        val noiseSettings = RegistryLookupCodec(InternalResourceKeys.NOISE_GENERATOR_SETTINGS).codec().parse(data).resultOrPartial { LOGGER.error("Noise settings registry: $it") }.orThrow("Failed to get noise settings registry")
        WorldGenerationSettings.makeDefault(dimensionTypes, biomes, noiseSettings)
    }
}

private fun Dynamic<*>.toDataPackConfig() = DataPackConfig.CODEC.parse(this).resultOrPartial(LOGGER::error).orElse(DataPackConfig.DEFAULT)
