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
package org.kryptonmc.krypton.entity.monster

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.monster.Zombie
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonZombie(world: KryptonWorld) : KryptonMonster(world, EntityTypes.ZOMBIE), Zombie {

    private var conversionTime = 0

    init {
        data += MetadataKeys.ZOMBIE.BABY
        data += MetadataKeys.ZOMBIE.CONVERTING
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        isBaby = tag.getBoolean("IsBaby")
        if (tag.contains("DrownedConversionTime", 99) && tag.getInt("DrownedConversionTime") > -1) {
            conversionTime = tag.getInt("DrownedConversionTime")
            isConverting = true
        }
    }

    override fun save() = super.save().apply {
        boolean("IsBaby", isBaby)
        int("DrownedConversionTime", if (isConverting) conversionTime else -1)
    }

    override var isBaby: Boolean
        get() = data[MetadataKeys.ZOMBIE.BABY]
        set(value) = data.set(MetadataKeys.ZOMBIE.BABY, value)

    override var isConverting: Boolean
        get() = data[MetadataKeys.ZOMBIE.CONVERTING]
        set(value) = data.set(MetadataKeys.ZOMBIE.CONVERTING, value)
}
