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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Goat
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonGoat(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.GOAT, ATTRIBUTES), Goat {

    override var canScream: Boolean
        get() = data[MetadataKeys.GOAT.SCREAMING]
        set(value) = data.set(MetadataKeys.GOAT.SCREAMING, value)

    init {
        data.add(MetadataKeys.GOAT.SCREAMING)
    }

    override fun onAgeTransformation() {
        attribute(AttributeTypes.ATTACK_DAMAGE)?.baseValue = if (isBaby) 1.0 else 2.0
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        canScream = tag.getBoolean("IsScreamingGoat")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("IsScreamingGoat", canScream)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.2)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
            .build()
    }
}
