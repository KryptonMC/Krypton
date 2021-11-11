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
package org.kryptonmc.krypton.entity.ambient

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.ambient.Bat
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonBat(world: KryptonWorld) : KryptonAmbientCreature(world, EntityTypes.BAT, ATTRIBUTES), Bat {

    override var isResting: Boolean
        get() = data[MetadataKeys.BAT.FLAGS].toInt() and 1 != 0
        set(value) {
            val old = data[MetadataKeys.BAT.FLAGS].toInt()
            data[MetadataKeys.BAT.FLAGS] = (if (value) old or 1 else old and -2).toByte()
        }

    init {
        data.add(MetadataKeys.BAT.FLAGS)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        data[MetadataKeys.BAT.FLAGS] = tag.getByte("BatFlags")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        byte("BatFlags", data[MetadataKeys.BAT.FLAGS])
    }

    companion object {

        private val ATTRIBUTES = KryptonMob.attributes()
            .add(AttributeTypes.MAX_HEALTH, 6.0)
            .build()
    }
}
