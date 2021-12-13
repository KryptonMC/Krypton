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
import org.kryptonmc.api.entity.animal.Ocelot
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonOcelot(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.OCELOT, ATTRIBUTES), Ocelot {

    override var isTrusting: Boolean
        get() = data[MetadataKeys.OCELOT.TRUSTING]
        set(value) = data.set(MetadataKeys.OCELOT.TRUSTING, value)

    init {
        data.add(MetadataKeys.OCELOT.TRUSTING)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        isTrusting = tag.getBoolean("Trusting")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("Trusting", isTrusting)
    }

    override fun isFood(item: ItemStack): Boolean = TEMPT_INGREDIENTS.contains(item.type)

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.ATTACK_DAMAGE, 3.0)
            .build()
        private val TEMPT_INGREDIENTS = setOf(ItemTypes.COD, ItemTypes.SALMON)
    }
}
