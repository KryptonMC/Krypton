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
package org.kryptonmc.krypton.entity.serializer.aquatic

import org.kryptonmc.krypton.entity.aquatic.KryptonDolphin
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.nbt.CompoundTag

object DolphinSerializer : EntitySerializer<KryptonDolphin> {

    private const val TREASURE_PREFIX = "TreasurePos"
    private const val GOT_FISH_TAG = "GotFish"
    private const val MOISTNESS_TAG = "Moistness"

    override fun load(entity: KryptonDolphin, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.treasurePosition = data.getBlockPos(TREASURE_PREFIX)
        entity.hasGotFish = data.getBoolean(GOT_FISH_TAG)
        entity.skinMoisture = data.getInt(MOISTNESS_TAG)
    }

    override fun save(entity: KryptonDolphin): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBlockPosParts(entity.treasurePosition, TREASURE_PREFIX)
        putBoolean(GOT_FISH_TAG, entity.hasGotFish)
        putInt(MOISTNESS_TAG, entity.skinMoisture)
    }
}
