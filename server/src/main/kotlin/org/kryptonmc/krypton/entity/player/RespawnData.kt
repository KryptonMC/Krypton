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
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.hasBlockPos
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.serialization.nbt.NbtOps

@JvmRecord
data class RespawnData(val position: BlockPos, val dimension: ResourceKey<World>, val angle: Float, val forced: Boolean) {

    fun save(data: CompoundTag.Builder, logger: Logger): CompoundTag.Builder = data.apply {
        putBlockPosParts(position, XYZ_PREFIX)
        putFloat(ANGLE_TAG, angle)
        putBoolean(FORCED_TAG, forced)
        Keys.CODEC.encodeStart(dimension.location, NbtOps.INSTANCE).resultOrPartial { logger.error(it) }.ifPresent { put(DIMENSION_TAG, it) }
    }

    companion object {

        private const val XYZ_PREFIX = "Spawn"
        private const val ANGLE_TAG = "SpawnAngle"
        private const val FORCED_TAG = "SpawnForced"
        private const val DIMENSION_TAG = "SpawnDimension"

        @JvmStatic
        fun load(data: CompoundTag, logger: Logger): RespawnData? {
            if (!data.hasBlockPos(XYZ_PREFIX)) return null
            val dimension = if (data.contains(DIMENSION_TAG, StringTag.ID)) {
                Codecs.DIMENSION.read(data.get(DIMENSION_TAG), NbtOps.INSTANCE).resultOrPartial { logger.error(it) }.orElse(World.OVERWORLD)
            } else {
                World.OVERWORLD
            }
            return RespawnData(data.getBlockPos(XYZ_PREFIX), dimension, data.getFloat(ANGLE_TAG), data.getBoolean(FORCED_TAG))
        }
    }
}
