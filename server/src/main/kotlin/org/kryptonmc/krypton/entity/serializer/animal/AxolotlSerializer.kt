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

import org.kryptonmc.krypton.entity.animal.KryptonAxolotl
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object AxolotlSerializer : EntitySerializer<KryptonAxolotl> {

    private const val VARIANT_TAG = "Variant"
    private const val FROM_BUCKET_TAG = "FromBucket"

    override fun load(entity: KryptonAxolotl, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.data.set(MetadataKeys.Axolotl.VARIANT, data.getInt(VARIANT_TAG))
        entity.setSpawnedFromBucket(data.getBoolean(FROM_BUCKET_TAG))
    }

    override fun save(entity: KryptonAxolotl): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putInt(VARIANT_TAG, entity.data.get(MetadataKeys.Axolotl.VARIANT))
        putBoolean(FROM_BUCKET_TAG, entity.wasSpawnedFromBucket())
    }
}
