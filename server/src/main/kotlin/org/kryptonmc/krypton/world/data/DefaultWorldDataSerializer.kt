/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.data

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class DefaultWorldDataSerializer(private val folder: Path) : WorldDataSerializer {

    override fun load(name: String): WorldData? {
        val path = folder.resolve(name)
        if (!Files.exists(path) || !Files.isDirectory(path)) return null
        val levelData = path.resolve("level.dat")
        if (Files.exists(levelData)) return read(path, levelData)
        val oldLevelData = path.resolve("level.dat_old")
        if (Files.exists(oldLevelData)) return read(path, oldLevelData)
        return null
    }

    private fun read(folder: Path, path: Path): PrimaryWorldData? {
        return try {
            val data = TagIO.read(path, TagCompression.GZIP).getCompound("Data")

            val version = if (data.contains("DataVersion", 99)) data.getInt("DataVersion") else -1
            val newData = if (version < KryptonPlatform.worldVersion) DataConversion.upgrade(data, MCTypeRegistry.LEVEL, version) else data

            PrimaryWorldData.parse(newData)
        } catch (exception: Exception) {
            LOGGER.error("Error whilst trying to read world at $folder!", exception)
            null
        }
    }

    override fun save(name: String, data: WorldData) {
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

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
