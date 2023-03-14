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
package org.kryptonmc.krypton.entity.serializer.monster

import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.nbt.CompoundTag

object ZombieSerializer : EntitySerializer<KryptonZombie> {

    private const val BABY_TAG = "IsBaby"
    private const val CONVERSION_TIME_TAG = "DrownedConversionTime"

    override fun load(entity: KryptonZombie, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.isBaby = data.getBoolean(BABY_TAG)
        if (data.hasNumber(CONVERSION_TIME_TAG) && data.getInt(CONVERSION_TIME_TAG) > -1) {
            entity.conversionTime = data.getInt(CONVERSION_TIME_TAG)
            entity.isConverting = true
        }
    }

    override fun save(entity: KryptonZombie): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBoolean(BABY_TAG, entity.isBaby)
        putInt(CONVERSION_TIME_TAG, entity.conversionTime)
    }
}
