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
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object ProjectileSerializer : EntitySerializer<KryptonProjectile> {

    private const val OWNER_TAG = "Owner"
    private const val LEFT_OWNER_TAG = "LeftOwner"
    private const val HAS_BEEN_SHOT_TAG = "HasBeenShot"

    override fun load(entity: KryptonProjectile, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        if (data.hasUUID(OWNER_TAG)) entity.setOwnerId(data.getUUID(OWNER_TAG))
        entity.setLeftOwner(data.getBoolean(LEFT_OWNER_TAG))
        entity.setBeenShot(data.getBoolean(HAS_BEEN_SHOT_TAG))
    }

    override fun save(entity: KryptonProjectile): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putNullable(OWNER_TAG, entity.ownerId(), CompoundTag.Builder::putUUID)
        if (entity.hasLeftOwner()) putBoolean(LEFT_OWNER_TAG, true)
        putBoolean(HAS_BEEN_SHOT_TAG, entity.hasBeenShot())
    }
}
