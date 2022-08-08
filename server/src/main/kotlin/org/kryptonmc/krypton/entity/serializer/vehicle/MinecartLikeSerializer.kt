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
package org.kryptonmc.krypton.entity.serializer.vehicle

import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.vehicle.KryptonMinecartLike
import org.kryptonmc.krypton.world.block.toBlockState
import org.kryptonmc.krypton.world.block.toNBT
import org.kryptonmc.nbt.CompoundTag

object MinecartLikeSerializer : EntitySerializer<KryptonMinecartLike> {

    override fun load(entity: KryptonMinecartLike, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        if (!data.getBoolean("CustomDisplayTile")) return
        entity.customBlock = data.getCompound("DisplayState").toBlockState()
        entity.customBlockOffset = data.getInt("DisplayOffset")
    }

    override fun save(entity: KryptonMinecartLike): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        if (!entity.hasCustomBlock) return@apply
        boolean("CustomDisplayTile", true)
        put("DisplayState", entity.customBlock.toNBT())
        int("DisplayOffset", entity.customBlockOffset)
    }
}
