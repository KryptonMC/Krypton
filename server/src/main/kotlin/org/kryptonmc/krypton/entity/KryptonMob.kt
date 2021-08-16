/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

abstract class KryptonMob(world: KryptonWorld, type: EntityType<out Mob>) : KryptonLivingEntity(world, type), Mob {

    override var canPickUpLoot = false
    override var isPersistent = false

    init {
        data.add(MetadataKeys.MOB.FLAGS)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        canPickUpLoot = tag.getBoolean("CanPickUpLoot")
        isPersistent = tag.getBoolean("PersistenceRequired")
        mainHand = if (tag.getBoolean("LeftHanded")) MainHand.LEFT else MainHand.RIGHT
        hasAI = !tag.getBoolean("NoAI")
    }

    override fun save() = super.save().apply {
        boolean("CanPickUpLoot", canPickUpLoot)
        boolean("PersistenceRequired", isPersistent)
        boolean("LeftHanded", mainHand == MainHand.LEFT)
        if (!hasAI) boolean("NoAI", true)
    }

    override var hasAI: Boolean
        get() = data[MetadataKeys.MOB.FLAGS].toInt() and 1 == 0
        set(value) {
            val flags = data[MetadataKeys.MOB.FLAGS].toInt()
            data[MetadataKeys.MOB.FLAGS] = (if (value) flags or 1 else flags and -2).toByte()
        }
    override var mainHand: MainHand
        get() = if (data[MetadataKeys.MOB.FLAGS].toInt() and 2 != 0) MainHand.LEFT else MainHand.RIGHT
        set(value) {
            val flags = data[MetadataKeys.MOB.FLAGS].toInt()
            data[MetadataKeys.MOB.FLAGS] = (if (value == MainHand.LEFT) flags or 1 else flags and -2).toByte()
        }
    override var isAggressive: Boolean
        get() = data[MetadataKeys.MOB.FLAGS].toInt() and 4 != 0
        set(value) {
            val flags = data[MetadataKeys.MOB.FLAGS].toInt()
            data[MetadataKeys.MOB.FLAGS] = (if (value) flags or 4 else flags and -5).toByte()
        }
}
