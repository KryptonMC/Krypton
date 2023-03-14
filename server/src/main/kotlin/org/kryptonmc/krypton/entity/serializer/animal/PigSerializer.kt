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
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.krypton.entity.animal.KryptonPig
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object PigSerializer : EntitySerializer<KryptonPig> {

    private const val SADDLE_TAG = "Saddle"

    override fun load(entity: KryptonPig, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.isSaddled = data.getBoolean(SADDLE_TAG)
    }

    override fun save(entity: KryptonPig): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putBoolean(SADDLE_TAG, entity.isSaddled)
    }
}
