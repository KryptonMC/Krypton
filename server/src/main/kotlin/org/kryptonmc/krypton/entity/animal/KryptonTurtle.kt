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

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Turtle
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

class KryptonTurtle(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.TURTLE, ATTRIBUTES), Turtle {

    override var hasEgg: Boolean
        get() = data[MetadataKeys.TURTLE.HAS_EGG]
        set(value) = data.set(MetadataKeys.TURTLE.HAS_EGG, value)
    override var isLayingEgg: Boolean
        get() = data[MetadataKeys.TURTLE.LAYING_EGG]
        set(value) = data.set(MetadataKeys.TURTLE.LAYING_EGG, value)
    override var isGoingHome: Boolean
        get() = data[MetadataKeys.TURTLE.GOING_HOME]
        set(value) = data.set(MetadataKeys.TURTLE.GOING_HOME, value)
    override var isTravelling: Boolean
        get() = data[MetadataKeys.TURTLE.TRAVELLING]
        set(value) = data.set(MetadataKeys.TURTLE.TRAVELLING, value)
    override var home: Vector3i
        get() = data[MetadataKeys.TURTLE.HOME]
        set(value) = data.set(MetadataKeys.TURTLE.HOME, value)
    override var destination: Vector3i
        get() = data[MetadataKeys.TURTLE.DESTINATION]
        set(value) = data.set(MetadataKeys.TURTLE.DESTINATION, value)

    override val pushedByFluid: Boolean
        get() = false
    override val swimSound: SoundEvent
        get() = SoundEvents.TURTLE_SWIM
    override val canFallInLove: Boolean
        get() = super.canFallInLove && !hasEgg

    init {
        data.add(MetadataKeys.TURTLE.HOME)
        data.add(MetadataKeys.TURTLE.HAS_EGG)
        data.add(MetadataKeys.TURTLE.LAYING_EGG)
        data.add(MetadataKeys.TURTLE.DESTINATION)
        data.add(MetadataKeys.TURTLE.GOING_HOME)
        data.add(MetadataKeys.TURTLE.TRAVELLING)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.SEAGRASS

    override fun load(tag: CompoundTag) {
        super.load(tag)
        val homeX = tag.getInt("HomePosX")
        val homeY = tag.getInt("HomePosY")
        val homeZ = tag.getInt("HomePosZ")
        home = Vector3i.from(homeX, homeY, homeZ)
        val destinationX = tag.getInt("TravelPosX")
        val destinationY = tag.getInt("TravelPosY")
        val destinationZ = tag.getInt("TravelPosZ")
        destination = Vector3i.from(destinationX, destinationY, destinationZ)
        hasEgg = tag.getBoolean("HasEgg")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("HomePosX", home.x())
        int("HomePosY", home.y())
        int("HomePosZ", home.z())
        boolean("HasEgg", hasEgg)
        int("TravelPosX", destination.x())
        int("TravelPosY", destination.y())
        int("TravelPosZ", destination.z())
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 30.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.25)
            .build()
    }
}
