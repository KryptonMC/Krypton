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

import org.kryptonmc.api.entity.animal.type.FoxVariant
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import org.kryptonmc.krypton.util.nbt.addNullable
import org.kryptonmc.krypton.util.nbt.addUUID
import org.kryptonmc.krypton.util.nbt.putStringEnum
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.list
import java.util.UUID

object FoxSerializer : EntitySerializer<KryptonFox> {

    private const val TRUSTED_TAG = "Trusted"
    private const val SLEEPING_TAG = "Sleeping"
    private const val TYPE_TAG = "Type"
    private const val SITTING_TAG = "Sitting"
    private const val CROUCHING_TAG = "Crouching"
    private val TYPE_NAMES = FoxVariant.values().associateBy { it.name.lowercase() }

    override fun load(entity: KryptonFox, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        data.getList(TRUSTED_TAG, IntArrayTag.ID).forEachIntArray { addTrustedId(entity, UUIDUtil.fromIntArray(it)) }
        entity.isSleeping = data.getBoolean(SLEEPING_TAG)
        if (data.contains(TYPE_TAG, StringTag.ID)) entity.variant = deserializeType(data.getString(TYPE_TAG))
        entity.isSitting = data.getBoolean(SITTING_TAG)
        entity.isCrouching = data.getBoolean(CROUCHING_TAG)
    }

    override fun save(entity: KryptonFox): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        list(TRUSTED_TAG) {
            addNullable(entity.firstTrusted, ListTag.Builder::addUUID)
            addNullable(entity.secondTrusted, ListTag.Builder::addUUID)
        }
        putBoolean(SLEEPING_TAG, entity.isSleeping)
        putStringEnum(TYPE_TAG, entity.variant)
        putBoolean(SITTING_TAG, entity.isSitting)
        putBoolean(CROUCHING_TAG, entity.isCrouching)
    }

    @JvmStatic
    private fun addTrustedId(entity: KryptonFox, uuid: UUID?) {
        if (entity.firstTrusted != null) entity.secondTrusted = uuid else entity.firstTrusted = uuid
    }

    @JvmStatic
    private fun deserializeType(name: String): FoxVariant = TYPE_NAMES.get(name)!!
}
