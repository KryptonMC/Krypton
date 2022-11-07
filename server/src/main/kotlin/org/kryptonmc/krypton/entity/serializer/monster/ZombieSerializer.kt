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

import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.nbt.CompoundTag

object ZombieSerializer : EntitySerializer<KryptonZombie> {

    private const val BABY_TAG = "IsBaby"
    private const val CONVERSION_TIME_TAG = "DrownedConversionTime"

    override fun load(entity: KryptonZombie, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.isBaby = data.getBoolean(BABY_TAG)
        if (data.hasNumber(CONVERSION_TIME_TAG) && data.getInt(CONVERSION_TIME_TAG) > -1) {
            entity.conversionTime = data.getInt(CONVERSION_TIME_TAG)
            entity.isConverting = true
        }
    }

    override fun save(entity: KryptonZombie): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBoolean(BABY_TAG, entity.isBaby)
        putInt(CONVERSION_TIME_TAG, entity.conversionTime)
    }
}
