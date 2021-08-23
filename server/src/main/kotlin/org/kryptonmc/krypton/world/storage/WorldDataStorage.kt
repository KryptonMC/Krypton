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

import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldResource
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.moveTo

class WorldDataStorage(val baseFolder: Path) {

    init {
        (if (baseFolder.exists()) baseFolder.toRealPath() else baseFolder).createDirectories()
    }

    fun createAccess(id: String) = WorldDataAccess(this, id)

    fun <T> loadData(folder: Path, reader: (Path) -> T): T? {
        if (!folder.exists()) return null
        var dataFile = folder.resolve(WorldResource.LEVEL_DATA_FILE.path)
        if (dataFile.exists()) reader(dataFile)?.let { return it }
        dataFile = folder.resolve("${WorldResource.LEVEL_DATA_FILE.path}_old")
        return if (dataFile.exists()) reader(dataFile) else null
    }

    fun saveData(folder: Path, data: PrimaryWorldData) {
        try {
            val temp = folder.createTempFile("level", ".dat")
            TagIO.write(temp, data.save(), TagCompression.GZIP)
            val dataPath = folder.resolve("level.dat")
            if (!dataPath.exists()) {
                temp.moveTo(dataPath)
                return
            }
            val oldDataPath = folder.resolve("level.dat_old").apply { deleteIfExists() }
            dataPath.moveTo(oldDataPath)
            dataPath.deleteIfExists()
            temp.moveTo(dataPath)
        } catch (exception: Exception) {
            LOGGER.error("Failed to save data for world ${data.name}!")
        }
    }

    companion object {

        private val LOGGER = logger<WorldDataStorage>()
    }
}
