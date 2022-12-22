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

import org.kryptonmc.krypton.entity.animal.KryptonAnimal
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object AnimalSerializer : EntitySerializer<KryptonAnimal> {

    private const val LOVE_TAG = "InLove"
    private const val LOVE_CAUSE_TAG = "LoveCause"

    override fun load(entity: KryptonAnimal, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.inLoveTime = data.getInt(LOVE_TAG)
        entity.loveCause = if (data.hasUUID(LOVE_CAUSE_TAG)) data.getUUID(LOVE_CAUSE_TAG) else null
    }

    override fun save(entity: KryptonAnimal): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putInt(LOVE_TAG, entity.inLoveTime)
        putNullable(LOVE_CAUSE_TAG, entity.loveCause, CompoundTag.Builder::putUUID)
    }
}
