/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.entity.player

import org.apache.logging.log4j.Logger
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.serialization.nbt.NbtOps
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class RespawnData(
    val position: Vector3i,
    val dimension: ResourceKey<World>,
    val angle: Float,
    val forced: Boolean
) {

    fun save(data: CompoundTag.Builder, logger: Logger): CompoundTag.Builder = data.apply {
        int("SpawnX", position.x())
        int("SpawnY", position.y())
        int("SpawnZ", position.z())
        float("SpawnAngle", angle)
        boolean("SpawnForced", forced)
        Codecs.KEY.encodeStart(dimension.location, NbtOps.INSTANCE).resultOrPartial(logger::error).ifPresent { put("SpawnDimension", it) }
    }
}
