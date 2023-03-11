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

import org.kryptonmc.krypton.entity.animal.KryptonTamable
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object TamableSerializer : EntitySerializer<KryptonTamable> {

    private const val OWNER_TAG = "Owner"
    private const val SITTING_TAG = "Sitting"

    override fun load(entity: KryptonTamable, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        // TODO: Fix this. The vanilla implementation is weird, and this doesn't make any sense. It also needs old
        //  user conversion stuff that we don't have yet.
        val ownerId = if (data.hasUUID(OWNER_TAG)) {
            data.getUUID(OWNER_TAG)
        } else {
            // Here's where we need to get the owner tag as a string and convert it if needed
            null
        }

        if (ownerId != null) {
            try {
                entity.data.set(MetadataKeys.Tamable.OWNER, ownerId)
                entity.isTamed = true
            } catch (_: Throwable) {
                entity.isTamed = false
            }
        }
        entity.isOrderedToSit = data.getBoolean(SITTING_TAG)
        entity.isSitting = entity.isOrderedToSit
    }

    override fun save(entity: KryptonTamable): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putNullable(OWNER_TAG, entity.data.get(MetadataKeys.Tamable.OWNER), CompoundTag.Builder::putUUID)
        putBoolean(SITTING_TAG, entity.isOrderedToSit)
    }
}
