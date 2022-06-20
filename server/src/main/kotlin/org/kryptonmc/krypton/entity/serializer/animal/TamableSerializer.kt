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

import org.kryptonmc.krypton.entity.animal.KryptonTamable
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object TamableSerializer : EntitySerializer<KryptonTamable> {

    override fun load(entity: KryptonTamable, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        // TODO: Fix this. The vanilla implementation is weird, and this doesn't make any sense. It also needs old
        //  user conversion stuff that we don't have yet.
        entity.data[MetadataKeys.TAMABLE.OWNER] = data.getUUID("Owner")
        entity.isTame = data.hasUUID("Owner")
        entity.isOrderedToSit = data.getBoolean("Sitting")
        entity.isSitting = entity.isOrderedToSit
    }

    override fun save(entity: KryptonTamable): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        val ownerUUID = entity.data[MetadataKeys.TAMABLE.OWNER]
        if (ownerUUID != null) uuid("Owner", ownerUUID)
        boolean("Sitting", entity.isOrderedToSit)
    }
}
