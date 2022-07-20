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
package org.kryptonmc.krypton.entity.animal

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Rabbit
import org.kryptonmc.api.entity.animal.type.RabbitVariant
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonRabbit(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.RABBIT, ATTRIBUTES), Rabbit {

    internal var moreCarrotTicks = 0
    override var variant: RabbitVariant
        get() {
            val id = data.get(MetadataKeys.Rabbit.TYPE)
            // It'll do you a treat mate!
            // Oh yeah?
            // Manky scot's git!
            // I'm warning you!
            // What's he do? Nibble ya bum?
            if (id == KILLER_TYPE) return RabbitVariant.KILLER
            return TYPES.getOrNull(data.get(MetadataKeys.Rabbit.TYPE)) ?: RabbitVariant.BROWN
        }
        set(value) {
            if (value == RabbitVariant.KILLER) {
                data.set(MetadataKeys.Rabbit.TYPE, KILLER_TYPE)
                return
            }
            data.set(MetadataKeys.Rabbit.TYPE, value.ordinal)
        }

    override val soundSource: Sound.Source
        get() = if (variant == RabbitVariant.KILLER) Sound.Source.HOSTILE else Sound.Source.NEUTRAL

    init {
        data.add(MetadataKeys.Rabbit.TYPE, 0)
    }

    override fun isFood(item: ItemStack): Boolean = TEMPTING_ITEMS.contains(item.type)

    companion object {

        private const val KILLER_TYPE = 99
        private val TYPES = RabbitVariant.values()
        private val ATTRIBUTES = attributes().add(AttributeTypes.MAX_HEALTH, 3.0).add(AttributeTypes.MOVEMENT_SPEED, 0.3).build()
        private val TEMPTING_ITEMS = setOf(ItemTypes.CARROT, ItemTypes.GOLDEN_CARROT, ItemTypes.DANDELION)
    }
}
