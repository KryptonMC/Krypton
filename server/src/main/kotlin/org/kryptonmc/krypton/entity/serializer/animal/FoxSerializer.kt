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

import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.StringTag
import java.util.UUID

object FoxSerializer : EntitySerializer<KryptonFox> {

    private val TYPE_NAMES = FoxType.values().associateBy { it.name.lowercase() }

    override fun load(entity: KryptonFox, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        data.getList("Trusted", IntArrayTag.ID).forEachIntArray { addTrustedId(entity, it.toUUID()) }
        entity.isSleeping = data.getBoolean("Sleeping")
        if (data.contains("Type", StringTag.ID)) entity.foxType = TYPE_NAMES[data.getString("Type")]!!
        entity.isSitting = data.getBoolean("Sitting")
        entity.isCrouching = data.getBoolean("Crouching")
    }

    override fun save(entity: KryptonFox): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        list("Trusted") {
            if (entity.firstTrusted != null) addUUID(entity.firstTrusted!!)
            if (entity.secondTrusted != null) addUUID(entity.secondTrusted!!)
        }
        boolean("Sleeping", entity.isSleeping)
        string("Type", entity.foxType.name.lowercase())
        boolean("Sitting", entity.isSitting)
        boolean("Crouching", entity.isCrouching)
    }

    @JvmStatic
    private fun addTrustedId(entity: KryptonFox, uuid: UUID?) {
        if (entity.data[MetadataKeys.FOX.FIRST_TRUSTED] != null) {
            entity.data[MetadataKeys.FOX.SECOND_TRUSTED] = uuid
        } else {
            entity.data[MetadataKeys.FOX.FIRST_TRUSTED] = uuid
        }
    }
}
