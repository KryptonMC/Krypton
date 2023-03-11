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
package org.kryptonmc.krypton.entity.player

import org.apache.logging.log4j.Logger
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.hasBlockPos
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.serialization.nbt.NbtOps

@JvmRecord
data class RespawnData(val position: Vec3i, val dimension: ResourceKey<World>, val angle: Float, val forced: Boolean) {

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
