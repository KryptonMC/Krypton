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
package org.kryptonmc.krypton.entity.monster

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.monster.Creeper
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonCreeper(world: KryptonWorld) : KryptonMonster(world, EntityTypes.CREEPER, ATTRIBUTES), Creeper {

    override var fuse: Short = 0
    override var explosionRadius: Int = 0

    override var isCharged: Boolean
        get() = data[MetadataKeys.CREEPER.CHARGED]
        set(value) = data.set(MetadataKeys.CREEPER.CHARGED, value)
    override var isIgnited: Boolean
        get() = data[MetadataKeys.CREEPER.IGNITED]
        set(value) = data.set(MetadataKeys.CREEPER.IGNITED, value)

    init {
        data.add(MetadataKeys.CREEPER.STATE)
        data.add(MetadataKeys.CREEPER.CHARGED)
        data.add(MetadataKeys.CREEPER.IGNITED)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        isCharged = tag.getBoolean("powered")
        isIgnited = tag.getBoolean("ignited")
        fuse = tag.getShort("Fuse")
        explosionRadius = tag.getInt("ExplosionRadius")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("powered", isCharged)
        boolean("ignited", isIgnited)
        short("Fuse", fuse)
        int("ExplosionRadius", explosionRadius)
    }

    companion object {

        private val ATTRIBUTES = attributes().add(AttributeTypes.MOVEMENT_SPEED, 0.25).build()
    }
}
