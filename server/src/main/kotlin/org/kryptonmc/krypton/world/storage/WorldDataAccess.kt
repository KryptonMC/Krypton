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
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.DirectoryLock
import org.kryptonmc.krypton.util.datafix.DataFixers
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.data.PrimaryWorldData
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
    private val lock = DirectoryLock.create(path)

    fun resolve(path: String): Path = this.path.resolve(path)

    fun loadData(ops: DynamicOps<Tag>, dataPackConfig: DataPackConfig): PrimaryWorldData? {
        checkLock()
        return storage.loadData(path, getWorldData(ops, dataPackConfig))
    }

    fun saveData(data: PrimaryWorldData) = storage.saveData(path, data)

    override fun close() = lock.close()

    private fun checkLock() {
        check(lock.isValid) { "Lock is no longer valid!" }
    }

    companion object {

        private val LOGGER = logger<WorldDataAccess>()

        private fun getWorldData(ops: DynamicOps<Tag>, dataPackConfig: DataPackConfig): (Path) -> PrimaryWorldData? = {
            try {
                val tag = TagIO.read(it, TagCompression.GZIP).getCompound("Data")
                val version = if (tag.contains("DataVersion", 99)) tag.getInt("DataVersion") else -1
                val data = DataFixers.get().update(References.LEVEL, Dynamic(ops, tag), version, KryptonPlatform.worldVersion)
                PrimaryWorldData.parse(data, WorldGenerationSettings.default(), dataPackConfig)
            } catch (exception: Exception) {
                LOGGER.error("Caught exception whilst trying to read $it!", exception)
                null
            }
        }
    }
}
