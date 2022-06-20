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
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.krypton.entity.KryptonAgeable
import org.kryptonmc.nbt.CompoundTag

object AgeableSerializer : EntitySerializer<KryptonAgeable> {

    override fun load(entity: KryptonAgeable, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.age = data.getInt("Age")
        entity.forcedAge = data.getInt("ForcedAge")
    }

    override fun save(entity: KryptonAgeable): CompoundTag.Builder = MobSerializer.save(entity).apply {
        int("Age", entity.age)
        int("ForcedAge", entity.forcedAge)
    }
}
