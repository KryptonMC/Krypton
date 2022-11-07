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

import org.kryptonmc.krypton.entity.hanging.KryptonHangingEntity
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.nbt.CompoundTag

object HangingEntitySerializer : EntitySerializer<KryptonHangingEntity> {

    private const val TILE_PREFIX = "Tile"

    override fun load(entity: KryptonHangingEntity, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.centerPosition = data.getBlockPos(TILE_PREFIX)
    }

    override fun save(entity: KryptonHangingEntity): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        entity.centerPosition?.let { putBlockPosParts(it, TILE_PREFIX) }
    }
}
