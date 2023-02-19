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
package org.kryptonmc.krypton.entity.serializer.monster

import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.nbt.CompoundTag

object CreeperSerializer : EntitySerializer<KryptonCreeper> {

    private const val POWERED_TAG = "powered"
    private const val IGNITED_TAG = "ignited"
    private const val FUSE_TAG = "Fuse"
    private const val EXPLOSION_RADIUS_TAG = "ExplosionRadius"

    override fun load(entity: KryptonCreeper, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.isCharged = data.getBoolean(POWERED_TAG)
        entity.setIgnited(data.getBoolean(IGNITED_TAG))
        entity.fuse = data.getShort(FUSE_TAG).toInt()
        entity.explosionRadius = data.getInt(EXPLOSION_RADIUS_TAG)
    }

    override fun save(entity: KryptonCreeper): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBoolean(POWERED_TAG, entity.isCharged)
        putBoolean(IGNITED_TAG, entity.isIgnited)
        putShort(FUSE_TAG, entity.fuse.toShort())
        putInt(EXPLOSION_RADIUS_TAG, entity.explosionRadius)
    }
}
