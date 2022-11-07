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

import org.kryptonmc.krypton.entity.KryptonExperienceOrb
import org.kryptonmc.nbt.CompoundTag

object ExperienceOrbSerializer : EntitySerializer<KryptonExperienceOrb> {

    private const val AGE_TAG = "Age"
    private const val COUNT_TAG = "Count"
    private const val HEALTH_TAG = "Health"
    private const val VALUE_TAG = "Value"

    override fun load(entity: KryptonExperienceOrb, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.age = data.getShort(AGE_TAG).toInt()
        entity.count = data.getInt(COUNT_TAG)
        entity.health = data.getShort(HEALTH_TAG).toInt()
        entity.experience = data.getShort(VALUE_TAG).toInt()
    }

    override fun save(entity: KryptonExperienceOrb): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putShort(AGE_TAG, entity.age.toShort())
        putInt(COUNT_TAG, entity.count)
        putShort(HEALTH_TAG, entity.health.toShort())
        putShort(VALUE_TAG, entity.experience.toShort())
    }
}
