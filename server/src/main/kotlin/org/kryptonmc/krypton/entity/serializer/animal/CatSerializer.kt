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

import org.kryptonmc.krypton.entity.animal.KryptonCat
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object CatSerializer : EntitySerializer<KryptonCat> {

    override fun load(entity: KryptonCat, data: CompoundTag) {
        TamableSerializer.load(entity, data)
        entity.data[MetadataKeys.CAT.VARIANT] = data.getInt("CatType")
        if (data.contains("CollarColor", 99)) entity.data[MetadataKeys.CAT.COLLAR_COLOR] = data.getInt("CollarColor")
    }

    override fun save(entity: KryptonCat): CompoundTag.Builder = TamableSerializer.save(entity).apply {
        int("CatType", entity.data[MetadataKeys.CAT.VARIANT])
        byte("CollarColor", entity.data[MetadataKeys.CAT.COLLAR_COLOR].toByte())
    }
}
