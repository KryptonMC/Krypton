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

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Fox
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

class KryptonFox(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.FOX, ATTRIBUTES), Fox {

    override var foxType: FoxType
        get() = FoxType.fromId(data[MetadataKeys.FOX.TYPE])!!
        set(value) = data.set(MetadataKeys.FOX.TYPE, value.ordinal)
    override var isSitting: Boolean
        get() = getFlag(0)
        set(value) = setFlag(0, value)
    override var isCrouching: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)
    override var isInterested: Boolean
        get() = getFlag(3)
        set(value) = setFlag(3, value)
    override var isPouncing: Boolean
        get() = getFlag(4)
        set(value) = setFlag(4, value)
    override var isSleeping: Boolean
        get() = getFlag(5)
        set(value) = setFlag(5, value)
    override var hasFaceplanted: Boolean
        get() = getFlag(6)
        set(value) = setFlag(6, value)
    override var isDefending: Boolean
        get() = getFlag(7)
        set(value) = setFlag(7, value)
    override var firstTrusted: UUID?
        get() = data[MetadataKeys.FOX.FIRST_TRUSTED]
        set(value) = data.set(MetadataKeys.FOX.FIRST_TRUSTED, value)
    override var secondTrusted: UUID?
        get() = data[MetadataKeys.FOX.SECOND_TRUSTED]
        set(value) = data.set(MetadataKeys.FOX.SECOND_TRUSTED, value)
    override var target: KryptonLivingEntity?
        get() = super.target
        set(value) {
            if (isDefending && value == null) isDefending = false
            super.target = value
        }

    init {
        data.add(MetadataKeys.FOX.FIRST_TRUSTED)
        data.add(MetadataKeys.FOX.SECOND_TRUSTED)
        data.add(MetadataKeys.FOX.TYPE)
        data.add(MetadataKeys.FOX.FLAGS)
    }

    override fun trusts(uuid: UUID): Boolean = uuid == firstTrusted || uuid == secondTrusted

    override fun isFood(item: ItemStack): Boolean = ItemTags.FOX_FOOD.contains(item.type)

    private fun getFlag(flag: Int): Boolean = getFlag(MetadataKeys.FOX.FLAGS, flag)

    private fun setFlag(flag: Int, state: Boolean) {
        setFlag(MetadataKeys.FOX.FLAGS, flag, state)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.FOLLOW_RANGE, 32.0)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
            .build()
    }
}
