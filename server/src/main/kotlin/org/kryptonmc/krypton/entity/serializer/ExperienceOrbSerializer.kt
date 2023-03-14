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
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.krypton.entity.KryptonExperienceOrb
import org.kryptonmc.nbt.CompoundTag

object ExperienceOrbSerializer : EntitySerializer<KryptonExperienceOrb> {

    private const val AGE_TAG = "Age"
    private const val COUNT_TAG = "Count"
    private const val HEALTH_TAG = "Health"
    private const val VALUE_TAG = "Value"

    override fun load(entity: KryptonExperienceOrb, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.age = data.getShort(AGE_TAG).toInt()
        entity.count = data.getInt(COUNT_TAG)
        entity.health = data.getShort(HEALTH_TAG).toInt()
        entity.experience = data.getShort(VALUE_TAG).toInt()
    }

    override fun save(entity: KryptonExperienceOrb): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putShort(AGE_TAG, entity.age.toShort())
        putInt(COUNT_TAG, entity.count)
        putShort(HEALTH_TAG, entity.health.toShort())
        putShort(VALUE_TAG, entity.experience.toShort())
    }
}
