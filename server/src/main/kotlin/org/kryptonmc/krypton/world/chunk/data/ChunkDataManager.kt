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
package org.kryptonmc.krypton.world.chunk.data

import com.mojang.serialization.Dynamic
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import java.nio.file.Path
import kotlin.math.max

open class ChunkDataManager(folder: Path, syncWrites: Boolean) : AutoCloseable {

    private val regionFileManager = RegionFileManager(folder, syncWrites)

    fun read(position: ChunkPosition) = regionFileManager.read(position)

    fun write(position: ChunkPosition, tag: CompoundTag) {
        regionFileManager.write(position, tag)
    }

    fun upgrade(dimension: ResourceKey<World>, tag: CompoundTag, position: ChunkPosition, world: KryptonWorld): CompoundTag {
        val version = if (tag.contains("DataVersion", 99)) tag.getInt("DataVersion") else -1
        var temp = tag
        if (version < 1493) {
            temp = DATA_FIXER.update(References.CHUNK, Dynamic(NBTOps, temp), version, 1493).value as CompoundTag
            // TODO: Legacy structure data
        }
        temp = DATA_FIXER.update(References.CHUNK, Dynamic(NBTOps, temp), max(1493, version), ServerInfo.WORLD_VERSION).value as CompoundTag
        if (version < ServerInfo.WORLD_VERSION) temp = temp.mutable().putInt("DataVersion", ServerInfo.WORLD_VERSION)
        return temp
    }

    override fun close() = regionFileManager.close()
}
