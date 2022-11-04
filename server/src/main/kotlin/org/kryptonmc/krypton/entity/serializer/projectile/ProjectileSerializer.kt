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
package org.kryptonmc.krypton.entity.serializer.projectile

import org.kryptonmc.krypton.entity.projectile.KryptonProjectile
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object ProjectileSerializer : EntitySerializer<KryptonProjectile> {

    override fun load(entity: KryptonProjectile, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        if (data.hasUUID("Owner")) entity.ownerId = data.getUUID("Owner")
        entity.hasLeftOwner = data.getBoolean("LeftOwner")
        entity.hasBeenShot = data.getBoolean("HasBeenShot")
    }

    override fun save(entity: KryptonProjectile): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        if (entity.ownerId != null) putUUID("Owner", entity.ownerId!!)
        if (entity.hasLeftOwner) putBoolean("LeftOwner", true)
        putBoolean("HasBeenShot", entity.hasBeenShot)
    }
}
