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
package org.kryptonmc.krypton.entity.serializer.vehicle

import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.vehicle.KryptonMinecartLike
import org.kryptonmc.krypton.world.block.BlockStateSerialization
import org.kryptonmc.nbt.CompoundTag

object MinecartLikeSerializer : EntitySerializer<KryptonMinecartLike> {

    private const val CUSTOM_DISPLAY_TAG = "CustomDisplayTile"
    private const val DISPLAY_STATE_TAG = "DisplayState"
    private const val DISPLAY_OFFSET_TAG = "DisplayOffset"

    override fun load(entity: KryptonMinecartLike, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        if (!data.getBoolean(CUSTOM_DISPLAY_TAG)) return
        entity.setCustomBlock(BlockStateSerialization.decode(data.getCompound(DISPLAY_STATE_TAG)))
        entity.customBlockOffset = data.getInt(DISPLAY_OFFSET_TAG)
    }

    override fun save(entity: KryptonMinecartLike): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        if (entity.showCustomBlock) {
            putBoolean(CUSTOM_DISPLAY_TAG, true)
            put(DISPLAY_STATE_TAG, BlockStateSerialization.encode(entity.customBlock()))
            putInt(DISPLAY_OFFSET_TAG, entity.customBlockOffset)
        }
    }
}
