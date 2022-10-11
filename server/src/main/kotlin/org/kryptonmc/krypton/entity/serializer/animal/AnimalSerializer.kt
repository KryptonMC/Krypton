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
import org.kryptonmc.nbt.CompoundTag

object AnimalSerializer : EntitySerializer<KryptonAnimal> {

    override fun load(entity: KryptonAnimal, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.inLoveTime = data.getInt("InLove")
        entity.loveCause = if (data.hasUUID("LoveCause")) data.getUUID("LoveCause") else null
    }

    override fun save(entity: KryptonAnimal): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        int("InLove", entity.inLoveTime)
        if (entity.loveCause != null) uuid("LoveCause", entity.loveCause!!)
    }
}
