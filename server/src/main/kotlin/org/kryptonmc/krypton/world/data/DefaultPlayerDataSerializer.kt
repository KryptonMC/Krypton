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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.player.PlayerSerializer
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

/**
 * Responsible for loading and saving player data files.
 */
class DefaultPlayerDataSerializer(val folder: Path) : PlayerDataSerializer {

    override fun loadById(uuid: UUID): CompoundTag? {
        val playerFile = folder.resolve("$uuid.dat")
        if (!Files.exists(playerFile)) {
            try {
                Files.createFile(playerFile)
            } catch (exception: Exception) {
                LOGGER.warn("Failed to create player file for player with UUID $uuid!", exception)
            }
            return null
        }

        return try {
            TagIO.read(playerFile, TagCompression.GZIP)
        } catch (exception: IOException) {
            LOGGER.warn("Failed to load player data for player $uuid!", exception)
            null
        }
    }

    override fun load(player: KryptonPlayer): CompoundTag? {
        val nbt = loadById(player.uuid) ?: return null

        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        val data = if (version < KryptonPlatform.worldVersion) DataConversion.upgrade(nbt, MCTypeRegistry.PLAYER, version) else nbt

        PlayerSerializer.load(player, data)
        return data
    }

    override fun save(player: KryptonPlayer): CompoundTag {
        val data = player.saveWithPassengers().build()

        // Create temp file and write data
        val temp = Files.createTempFile(folder, player.uuid.toString(), ".dat")
        TagIO.write(temp, data, TagCompression.GZIP)

        // Resolve actual file, and if it doesn't exist, rename the temp file
        val dataPath = folder.resolve("${player.uuid}.dat")
        if (!Files.exists(dataPath)) {
            Files.move(temp, dataPath, StandardCopyOption.REPLACE_EXISTING)
            return data
        }

        // Save the old data and then save the new data
        val oldDataPath = folder.resolve("${player.uuid}.dat_old")
        Files.deleteIfExists(oldDataPath)
        Files.move(dataPath, oldDataPath, StandardCopyOption.REPLACE_EXISTING)
        Files.deleteIfExists(dataPath)
        Files.move(temp, dataPath, StandardCopyOption.REPLACE_EXISTING)
        Files.deleteIfExists(temp)
        return data
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
