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
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putKeyed
import org.kryptonmc.nbt.CompoundTag

object PaintingSerializer : EntitySerializer<KryptonPainting> {

    private const val VARIANT_TAG = "variant"
    private const val FACING_TAG = "facing"

    override fun load(entity: KryptonPainting, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.variant = Registries.PAINTING_VARIANT.get(Key.key(data.getString(VARIANT_TAG)))
        entity.direction = Directions.of2D(data.getByte(FACING_TAG).toInt())
    }

    override fun save(entity: KryptonPainting): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putNullable(VARIANT_TAG, entity.variant, CompoundTag.Builder::putKeyed)
        putByte(FACING_TAG, Directions.data2D(entity.direction).toByte())
    }
}
