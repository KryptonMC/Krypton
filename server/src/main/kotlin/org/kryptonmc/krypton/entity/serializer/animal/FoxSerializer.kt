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
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.addUUID
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.list
import java.util.UUID

object FoxSerializer : EntitySerializer<KryptonFox> {

    private val TYPE_NAMES = FoxVariant.values().associateBy { it.name.lowercase() }

    override fun load(entity: KryptonFox, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        data.getList("Trusted", IntArrayTag.ID).forEachIntArray { addTrustedId(entity, it.toUUID()) }
        entity.isSleeping = data.getBoolean("Sleeping")
        if (data.contains("Type", StringTag.ID)) entity.variant = TYPE_NAMES.get(data.getString("Type"))!!
        entity.isSitting = data.getBoolean("Sitting")
        entity.isCrouching = data.getBoolean("Crouching")
    }

    override fun save(entity: KryptonFox): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        list("Trusted") {
            if (entity.firstTrusted != null) addUUID(entity.firstTrusted!!)
            if (entity.secondTrusted != null) addUUID(entity.secondTrusted!!)
        }
        putBoolean("Sleeping", entity.isSleeping)
        putString("Type", entity.variant.name.lowercase())
        putBoolean("Sitting", entity.isSitting)
        putBoolean("Crouching", entity.isCrouching)
    }

    @JvmStatic
    private fun addTrustedId(entity: KryptonFox, uuid: UUID?) {
        if (entity.data.get(MetadataKeys.Fox.FIRST_TRUSTED) != null) {
            entity.data.set(MetadataKeys.Fox.SECOND_TRUSTED, uuid)
        } else {
            entity.data.set(MetadataKeys.Fox.FIRST_TRUSTED, uuid)
        }
    }
}
