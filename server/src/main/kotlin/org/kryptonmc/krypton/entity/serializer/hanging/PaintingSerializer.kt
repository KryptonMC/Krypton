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
package org.kryptonmc.krypton.entity.serializer.hanging

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.data2D
import org.kryptonmc.nbt.CompoundTag

object PaintingSerializer : EntitySerializer<KryptonPainting> {

    override fun load(entity: KryptonPainting, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.picture = Registries.PICTURES.get(Key.key(data.getString("Motive")))
        entity.direction = Directions.of2D(data.getByte("Facing").toInt())
    }

    override fun save(entity: KryptonPainting): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        if (entity.picture != null) string("Motive", entity.picture!!.key().asString())
        byte("Facing", entity.direction.data2D().toByte())
    }
}
