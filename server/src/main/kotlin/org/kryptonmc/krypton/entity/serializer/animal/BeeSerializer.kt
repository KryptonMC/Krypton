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

import org.kryptonmc.krypton.entity.Neutral
import org.kryptonmc.krypton.entity.animal.KryptonBee
import org.kryptonmc.krypton.entity.getVector3i
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.putVector3i
import org.kryptonmc.nbt.CompoundTag

object BeeSerializer : EntitySerializer<KryptonBee> {

    override fun load(entity: KryptonBee, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        Neutral.loadAngerData(entity, data)
        entity.hive = data.getVector3i("HivePos")
        entity.flower = data.getVector3i("FlowerPos")
        entity.hasNectar = data.getBoolean("HasNectar")
        entity.hasStung = data.getBoolean("HasStung")
        entity.timeSincePollination = data.getInt("TicksSincePollination")
        entity.cannotEnterHiveTicks = data.getInt("CannotEnterHiveTicks")
        entity.cropsGrownSincePollination = data.getInt("CropsGrownSincePollination")
    }

    override fun save(entity: KryptonBee): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        Neutral.saveAngerData(entity, this)
        if (entity.hive != null) putVector3i("HivePos", entity.hive!!)
        if (entity.flower != null) putVector3i("FlowerPos", entity.flower!!)
        putBoolean("HasNectar", entity.hasNectar)
        putBoolean("HasStung", entity.hasStung)
        putInt("TicksSincePollination", entity.timeSincePollination)
        putInt("CannotEnterHiveTicks", entity.cannotEnterHiveTicks)
        putInt("CropsGrownSincePollination", entity.cropsGrownSincePollination)
    }
}
