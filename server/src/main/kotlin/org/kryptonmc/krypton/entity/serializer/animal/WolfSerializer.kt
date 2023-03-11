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

import org.kryptonmc.krypton.entity.components.Neutral
import org.kryptonmc.krypton.entity.animal.KryptonWolf
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.nbt.CompoundTag

object WolfSerializer : EntitySerializer<KryptonWolf> {

    private const val COLLAR_COLOR_TAG = "CollarColor"

    override fun load(entity: KryptonWolf, data: CompoundTag) {
        TamableSerializer.load(entity, data)
        if (data.hasNumber(COLLAR_COLOR_TAG)) entity.data.set(MetadataKeys.Wolf.COLLAR_COLOR, data.getInt(COLLAR_COLOR_TAG))
        Neutral.loadAngerData(entity, data)
    }

    override fun save(entity: KryptonWolf): CompoundTag.Builder = TamableSerializer.save(entity).apply {
        putInt(COLLAR_COLOR_TAG, entity.data.get(MetadataKeys.Wolf.COLLAR_COLOR))
        Neutral.saveAngerData(entity, this)
    }
}
