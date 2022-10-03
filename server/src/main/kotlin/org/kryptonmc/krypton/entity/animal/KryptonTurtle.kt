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
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i

class KryptonTurtle(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.TURTLE), Turtle {

    override var hasEgg: Boolean
        get() = data.get(MetadataKeys.Turtle.HAS_EGG)
        set(value) = data.set(MetadataKeys.Turtle.HAS_EGG, value)
    override var isLayingEgg: Boolean
        get() = data.get(MetadataKeys.Turtle.LAYING_EGG)
        set(value) = data.set(MetadataKeys.Turtle.LAYING_EGG, value)
    override var isGoingHome: Boolean
        get() = data.get(MetadataKeys.Turtle.GOING_HOME)
        set(value) = data.set(MetadataKeys.Turtle.GOING_HOME, value)
    override var isTravelling: Boolean
        get() = data.get(MetadataKeys.Turtle.TRAVELLING)
        set(value) = data.set(MetadataKeys.Turtle.TRAVELLING, value)
    override var home: Vector3i
        get() = data.get(MetadataKeys.Turtle.HOME)
        set(value) = data.set(MetadataKeys.Turtle.HOME, value)
    override var destination: Vector3i
        get() = data.get(MetadataKeys.Turtle.DESTINATION)
        set(value) = data.set(MetadataKeys.Turtle.DESTINATION, value)

    override val pushedByFluid: Boolean
        get() = false
    override val swimSound: SoundEvent
        get() = SoundEvents.TURTLE_SWIM
    override val canFallInLove: Boolean
        get() = super.canFallInLove && !hasEgg

    init {
        data.add(MetadataKeys.Turtle.HOME, Vector3i.ZERO)
        data.add(MetadataKeys.Turtle.HAS_EGG, false)
        data.add(MetadataKeys.Turtle.LAYING_EGG, false)
        data.add(MetadataKeys.Turtle.DESTINATION, Vector3i.ZERO)
        data.add(MetadataKeys.Turtle.GOING_HOME, false)
        data.add(MetadataKeys.Turtle.TRAVELLING, false)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.SEAGRASS

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(AttributeTypes.MAX_HEALTH, 30.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.25)
    }
}
