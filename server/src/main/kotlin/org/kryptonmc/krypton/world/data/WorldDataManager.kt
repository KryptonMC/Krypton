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

import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.converter.MCDataConverter.convertDataTyped
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTMapType
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.tryCreateDirectories
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.moveTo

class WorldDataManager(
    private val folder: Path,
    private val useDataConverter: Boolean
) {

    fun load(name: String, dataPackConfig: DataPackConfig = DataPackConfig.DEFAULT): PrimaryWorldData? {
        val path = folder.resolve(name)
        if (!path.exists() || !path.isDirectory()) return null
        val levelData = path.resolve("level.dat")
        if (levelData.exists()) return read(path, levelData, dataPackConfig)
        val oldLevelData = path.resolve("level.dat_old")
        if (oldLevelData.exists()) return read(path, oldLevelData, dataPackConfig)
        return null
    }

    fun save(name: String, data: PrimaryWorldData) {
        try {
            val path = folder.resolve(name)
            if (!path.exists()) path.tryCreateDirectories()
            val temp = path.createTempFile("level", ".dat")
            TagIO.write(temp, data.save(), TagCompression.GZIP)
            val dataPath = path.resolve("level.dat")
            if (!dataPath.exists()) {
                temp.moveTo(dataPath)
                return
            }
            val oldDataPath = path.resolve("level.dat_old").apply { deleteIfExists() }
            dataPath.moveTo(oldDataPath)
            dataPath.deleteIfExists()
            temp.moveTo(dataPath)
        } catch (exception: Exception) {
            LOGGER.error("Failed to save data for world ${data.name}!", exception)
        }
    }

    private fun read(folder: Path, path: Path, dataPackConfig: DataPackConfig): PrimaryWorldData? = try {
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
            tag.convertDataTyped(MCTypeRegistry.LEVEL, version, KryptonPlatform.worldVersion)
        } else {
            NBTMapType(tag)
        }
        PrimaryWorldData.parse(folder, data, WorldGenerationSettings.default(), dataPackConfig)
    } catch (exception: Exception) {
        null
    }

    companion object {

        private val LOGGER = logger<WorldDataManager>()
    }
}
