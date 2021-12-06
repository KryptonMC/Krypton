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
import org.kryptonmc.api.entity.animal.Chicken
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import kotlin.random.Random

class KryptonChicken(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.CHICKEN, ATTRIBUTES), Chicken {

    override var eggTime = Random.nextInt(6000) + 6000
    override var isJockey = false

    override fun isFood(item: ItemStack): Boolean = FOOD_ITEMS.contains(item.type)

    override fun load(tag: CompoundTag) {
        super.load(tag)
        isJockey = tag.getBoolean("IsChickenJockey")
        if (tag.contains("EggLayTime")) eggTime = tag.getInt("EggLayTime")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("IsChickenJockey", isJockey)
        int("EggLayTime", eggTime)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 4.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.25)
            .build()
        private val FOOD_ITEMS = setOf(
            ItemTypes.WHEAT_SEEDS,
            ItemTypes.MELON_SEEDS,
            ItemTypes.PUMPKIN_SEEDS,
            ItemTypes.BEETROOT_SEEDS
        )
    }
}
