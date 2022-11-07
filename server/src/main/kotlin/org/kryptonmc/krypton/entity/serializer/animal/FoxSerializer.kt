/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.api.entity.animal.type.FoxVariant
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.addNullable
import org.kryptonmc.krypton.util.nbt.addUUID
import org.kryptonmc.krypton.util.nbt.putStringEnum
import org.kryptonmc.krypton.util.toUUID
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
        data.getList(TRUSTED_TAG, IntArrayTag.ID).forEachIntArray { addTrustedId(entity, it.toUUID()) }
        entity.isSleeping = data.getBoolean(SLEEPING_TAG)
        if (data.contains(TYPE_TAG, StringTag.ID)) entity.variant = TYPE_NAMES.get(data.getString(TYPE_TAG))!!
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
}
