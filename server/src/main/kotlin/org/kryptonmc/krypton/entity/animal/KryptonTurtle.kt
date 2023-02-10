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

import org.kryptonmc.api.entity.animal.Turtle
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.TurtleSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonTurtle(world: KryptonWorld) : KryptonAnimal(world), Turtle {

    override val type: KryptonEntityType<KryptonTurtle>
        get() = KryptonEntityTypes.TURTLE
    override val serializer: EntitySerializer<KryptonTurtle>
        get() = TurtleSerializer

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
    override var home: Vec3i
        get() = data.get(MetadataKeys.Turtle.HOME)
        set(value) = data.set(MetadataKeys.Turtle.HOME, value)
    override var destination: Vec3i
        get() = data.get(MetadataKeys.Turtle.DESTINATION)
        set(value) = data.set(MetadataKeys.Turtle.DESTINATION, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Turtle.HOME, Vec3i.ZERO)
        data.define(MetadataKeys.Turtle.HAS_EGG, false)
        data.define(MetadataKeys.Turtle.LAYING_EGG, false)
        data.define(MetadataKeys.Turtle.DESTINATION, Vec3i.ZERO)
        data.define(MetadataKeys.Turtle.GOING_HOME, false)
        data.define(MetadataKeys.Turtle.TRAVELLING, false)
    }

    override fun canFallInLove(): Boolean = super.canFallInLove() && !hasEgg

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.SEAGRASS

    override fun isPushedByFluid(): Boolean = false

    companion object {

        private const val DEFAULT_MAX_HEALTH = 30.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.25

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
    }
}
