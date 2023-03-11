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

import org.kryptonmc.krypton.entity.aquatic.KryptonPufferfish
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object PufferfishSerializer : EntitySerializer<KryptonPufferfish> {

    private const val PUFF_STATE_TAG = "PuffState"

    override fun load(entity: KryptonPufferfish, data: CompoundTag) {
        FishSerializer.load(entity, data)
        entity.data.set(MetadataKeys.Pufferfish.PUFF_STATE, data.getInt(PUFF_STATE_TAG))
    }

    override fun save(entity: KryptonPufferfish): CompoundTag.Builder = FishSerializer.save(entity).apply {
        putInt(PUFF_STATE_TAG, entity.data.get(MetadataKeys.Pufferfish.PUFF_STATE))
    }
}
