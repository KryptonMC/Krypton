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
package org.kryptonmc.krypton.world.data

import ca.spottedleaf.dataconverter.minecraft.MCDataConverter
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import ca.spottedleaf.dataconverter.types.nbt.NBTMapType
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class WorldDataManager(private val folder: Path, private val useDataConverter: Boolean) {

    fun load(name: String, dataPackConfig: DataPackConfig = DataPackConfig.DEFAULT): PrimaryWorldData? {
        val path = folder.resolve(name)
        if (!Files.exists(path) || !Files.isDirectory(path)) return null
        val levelData = path.resolve("level.dat")
        if (Files.exists(levelData)) return read(path, levelData, dataPackConfig)
        val oldLevelData = path.resolve("level.dat_old")
        if (Files.exists(oldLevelData)) return read(path, oldLevelData, dataPackConfig)
        return null
    }

    fun save(name: String, data: PrimaryWorldData) {
        try {
            val path = folder.resolve(name)
            if (!Files.exists(path)) Files.createDirectories(path)

            val temp = Files.createTempFile(path, "level", ".dat")
            TagIO.write(temp, data.save(), TagCompression.GZIP)

            val dataPath = path.resolve("level.dat")
            if (!Files.exists(dataPath)) {
                Files.move(temp, dataPath, StandardCopyOption.REPLACE_EXISTING)
                return
            }

            val oldDataPath = path.resolve("level.dat_old")
            Files.deleteIfExists(oldDataPath)
            Files.move(dataPath, oldDataPath, StandardCopyOption.REPLACE_EXISTING)
            Files.deleteIfExists(dataPath)
            Files.move(temp, dataPath, StandardCopyOption.REPLACE_EXISTING)
        } catch (exception: Exception) {
            LOGGER.error("Failed to save data for world ${data.name}!", exception)
        }
    }

    fun resolve(resource: String): Path {
        try {
            val path = folder.resolve(resource)
            if (!Files.exists(path)) Files.createDirectory(path)
            return path
        } catch (exception: Exception) {
            LOGGER.error("Failed to create necessary directory $resource in world folder!", exception)
            throw exception
        }
    }

    private fun read(folder: Path, path: Path, dataPackConfig: DataPackConfig): PrimaryWorldData? {
        return try {
            val tag = TagIO.read(path, TagCompression.GZIP).getCompound("Data")
            val version = if (tag.contains("DataVersion", 99)) tag.getInt("DataVersion") else -1
            // We won't upgrade data if use of the data converter is disabled.
            if (version < KryptonPlatform.worldVersion && !useDataConverter) {
                LOGGER.error("The server attempted to load a world from an earlier version of Minecraft, but data conversion is disabled!")
                LOGGER.info("If you would like to use data conversion, provide the --upgrade-data flag to the JAR on startup.")
                LOGGER.warn("Beware that this is an experimental tool and has known issues with pre-1.13 worlds.")
                LOGGER.warn("USE THIS TOOL AT YOUR OWN RISK. If the tool corrupts your data, that is YOUR responsibility!")
                error("Attempted to load old world data from version $version when data conversion is disabled!")
            }

            // Don't use data converter if the data isn't older than our version.
            val data = if (useDataConverter && version < KryptonPlatform.worldVersion) {
                NBTMapType(MCDataConverter.convertTag(MCTypeRegistry.LEVEL, tag, version, KryptonPlatform.worldVersion))
            } else {
                NBTMapType(tag)
            }
            PrimaryWorldData.parse(folder, data, WorldGenerationSettings.default(), dataPackConfig)
        } catch (exception: Exception) {
            LOGGER.error("Error whilst trying to read world at $folder!", exception)
            null
        }
    }

    companion object {

        private val LOGGER = logger<WorldDataManager>()
    }
}
