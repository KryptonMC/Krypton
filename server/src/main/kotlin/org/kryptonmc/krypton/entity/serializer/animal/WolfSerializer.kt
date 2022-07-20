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

import org.kryptonmc.krypton.entity.Neutral
import org.kryptonmc.krypton.entity.animal.KryptonWolf
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object WolfSerializer : EntitySerializer<KryptonWolf> {

    override fun load(entity: KryptonWolf, data: CompoundTag) {
        TamableSerializer.load(entity, data)
        if (data.contains("CollarColor", 99)) entity.data.set(MetadataKeys.Wolf.COLLAR_COLOR, data.getInt("CollarColor"))
        Neutral.loadAngerData(entity, data)
    }

    override fun save(entity: KryptonWolf): CompoundTag.Builder = TamableSerializer.save(entity).apply {
        int("CollarColor", entity.data.get(MetadataKeys.Wolf.COLLAR_COLOR))
        Neutral.saveAngerData(entity, this)
    }
}
