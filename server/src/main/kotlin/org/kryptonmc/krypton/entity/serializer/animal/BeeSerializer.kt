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

import org.kryptonmc.krypton.entity.components.Neutral
import org.kryptonmc.krypton.entity.animal.KryptonBee
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.getNullableCompound
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putBlockPos
import org.kryptonmc.nbt.CompoundTag

object BeeSerializer : EntitySerializer<KryptonBee> {

    private const val HIVE_TAG = "HivePos"
    private const val FLOWER_TAG = "FlowerPos"
    private const val NECTAR_TAG = "HasNectar"
    private const val STUNG_TAG = "HasStung"
    private const val TICKS_SINCE_POLLINATION_TAG = "TicksSincePollination"
    private const val CANNOT_ENTER_HIVE_TICKS_TAG = "CannotEnterHiveTicks"
    private const val CROPS_GROWN_SINCE_POLLINATION_TAG = "CropsGrownSincePollination"

    override fun load(entity: KryptonBee, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        Neutral.loadAngerData(entity, data)
        entity.hive = data.getNullableCompound(HIVE_TAG)?.getBlockPos()
        entity.flower = data.getNullableCompound(FLOWER_TAG)?.getBlockPos()
        entity.hasNectar = data.getBoolean(NECTAR_TAG)
        entity.hasStung = data.getBoolean(STUNG_TAG)
        entity.timeSincePollination = data.getInt(TICKS_SINCE_POLLINATION_TAG)
        entity.cannotEnterHiveTicks = data.getInt(CANNOT_ENTER_HIVE_TICKS_TAG)
        entity.cropsGrownSincePollination = data.getInt(CROPS_GROWN_SINCE_POLLINATION_TAG)
    }

    override fun save(entity: KryptonBee): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        Neutral.saveAngerData(entity, this)
        putNullable(HIVE_TAG, entity.hive, CompoundTag.Builder::putBlockPos)
        putNullable(FLOWER_TAG, entity.flower, CompoundTag.Builder::putBlockPos)
        putBoolean(NECTAR_TAG, entity.hasNectar)
        putBoolean(STUNG_TAG, entity.hasStung)
        putInt(TICKS_SINCE_POLLINATION_TAG, entity.timeSincePollination)
        putInt(CANNOT_ENTER_HIVE_TICKS_TAG, entity.cannotEnterHiveTicks)
        putInt(CROPS_GROWN_SINCE_POLLINATION_TAG, entity.cropsGrownSincePollination)
    }
}
