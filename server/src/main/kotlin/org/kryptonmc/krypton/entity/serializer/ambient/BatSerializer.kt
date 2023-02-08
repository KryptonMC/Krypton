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
package org.kryptonmc.krypton.entity.serializer.ambient

import org.kryptonmc.krypton.entity.ambient.KryptonBat
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.nbt.CompoundTag

object BatSerializer : EntitySerializer<KryptonBat> {

    private const val FLAGS_TAG = "BatFlags"

    override fun load(entity: KryptonBat, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.data.set(MetadataKeys.Bat.FLAGS, data.getByte(FLAGS_TAG))
    }

    override fun save(entity: KryptonBat): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putByte(FLAGS_TAG, entity.data.get(MetadataKeys.Bat.FLAGS))
    }
}