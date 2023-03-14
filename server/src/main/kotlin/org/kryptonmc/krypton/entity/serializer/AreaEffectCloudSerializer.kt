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

import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.entity.KryptonAreaEffectCloud
import org.kryptonmc.nbt.CompoundTag

object AreaEffectCloudSerializer : EntitySerializer<KryptonAreaEffectCloud> {

    private const val AGE_TAG = "Age"
    private const val DURATION_TAG = "Duration"
    private const val RADIUS_TAG = "Radius"
    private const val COLOR_TAG = "Color"

    override fun load(entity: KryptonAreaEffectCloud, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.age = data.getInt(AGE_TAG)
        entity.duration = data.getInt(DURATION_TAG)
        entity.radius = data.getFloat(RADIUS_TAG)
        entity.color = Color(data.getInt(COLOR_TAG))
    }

    override fun save(entity: KryptonAreaEffectCloud): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putInt(AGE_TAG, entity.age)
        putInt(DURATION_TAG, entity.duration)
        putFloat(RADIUS_TAG, entity.radius)
        putInt(COLOR_TAG, entity.color.encode())
    }
}
