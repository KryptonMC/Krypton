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

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Rabbit
import org.kryptonmc.api.entity.animal.type.RabbitType
import org.kryptonmc.api.entity.animal.type.RabbitTypes
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonRabbit(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.RABBIT, ATTRIBUTES), Rabbit {

    private var moreCarrotTicks = 0
    override var rabbitType: RabbitType
        get() {
            val id = data[MetadataKeys.RABBIT.TYPE]
            if (id == 99) return RabbitTypes.KILLER
            return Registries.RABBIT_TYPES[data[MetadataKeys.RABBIT.TYPE]]!!
        }
        set(value) {
            if (value === RabbitTypes.KILLER) {
                data[MetadataKeys.RABBIT.TYPE] = 99
                return
            }
            data[MetadataKeys.RABBIT.TYPE] = Registries.RABBIT_TYPES.idOf(value)
        }

    override val soundSource: Sound.Source
        get() = if (rabbitType === RabbitTypes.KILLER) Sound.Source.HOSTILE else Sound.Source.NEUTRAL

    init {
        data.add(MetadataKeys.RABBIT.TYPE)
    }

    override fun isFood(item: ItemStack): Boolean = TEMPTING_ITEMS.contains(item.type)

    override fun load(tag: CompoundTag) {
        super.load(tag)
        data[MetadataKeys.RABBIT.TYPE] = tag.getInt("RabbitType")
        moreCarrotTicks = tag.getInt("MoreCarrotTicks")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("RabbitType", data[MetadataKeys.RABBIT.TYPE])
        int("MoreCarrotTicks", moreCarrotTicks)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 3.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .build()
        private val TEMPTING_ITEMS = setOf(
            ItemTypes.CARROT,
            ItemTypes.GOLDEN_CARROT,
            ItemTypes.DANDELION
        )
    }
}
