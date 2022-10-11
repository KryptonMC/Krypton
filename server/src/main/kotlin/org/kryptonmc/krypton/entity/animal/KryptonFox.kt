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

import org.kryptonmc.api.entity.animal.Fox
import org.kryptonmc.api.entity.animal.type.FoxVariant
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.FoxSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

class KryptonFox(world: KryptonWorld) : KryptonAnimal(world), Fox {

    override val type: KryptonEntityType<Fox>
        get() = KryptonEntityTypes.FOX
    override val serializer: EntitySerializer<KryptonFox>
        get() = FoxSerializer

    override var variant: FoxVariant
        get() = TYPES.getOrNull(data.get(MetadataKeys.Fox.TYPE)) ?: FoxVariant.RED
        set(value) = data.set(MetadataKeys.Fox.TYPE, value.ordinal)
    override var isSitting: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_SITTING)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_SITTING, value)
    override var isCrouching: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_CROUCHING)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_CROUCHING, value)
    override var isInterested: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_INTERESTED)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_INTERESTED, value)
    override var isPouncing: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_POUNCING)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_POUNCING, value)
    override var isSleeping: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_SLEEPING)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_SLEEPING, value)
    override var hasFaceplanted: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_FACEPLANTED)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_FACEPLANTED, value)
    override var isDefending: Boolean
        get() = data.getFlag(MetadataKeys.Fox.FLAGS, FLAG_DEFENDING)
        set(value) = data.setFlag(MetadataKeys.Fox.FLAGS, FLAG_DEFENDING, value)
    override var firstTrusted: UUID?
        get() = data.get(MetadataKeys.Fox.FIRST_TRUSTED)
        set(value) = data.set(MetadataKeys.Fox.FIRST_TRUSTED, value)
    override var secondTrusted: UUID?
        get() = data.get(MetadataKeys.Fox.SECOND_TRUSTED)
        set(value) = data.set(MetadataKeys.Fox.SECOND_TRUSTED, value)
    override var target: KryptonLivingEntity?
        get() = super.target
        set(value) {
            if (isDefending && value == null) isDefending = false
            super.target = value
        }

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Fox.TYPE, FoxVariant.RED.ordinal)
        data.define(MetadataKeys.Fox.FLAGS, 0)
        data.define(MetadataKeys.Fox.FIRST_TRUSTED, null)
        data.define(MetadataKeys.Fox.SECOND_TRUSTED, null)
    }

    override fun trusts(uuid: UUID): Boolean = uuid == firstTrusted || uuid == secondTrusted

    override fun isFood(item: ItemStack): Boolean = ItemTags.FOX_FOOD.contains(item.type)

    companion object {

        private const val FLAG_SITTING = 0
        private const val FLAG_CROUCHING = 2
        private const val FLAG_INTERESTED = 3
        private const val FLAG_POUNCING = 4
        private const val FLAG_SLEEPING = 5
        private const val FLAG_FACEPLANTED = 6
        private const val FLAG_DEFENDING = 7
        private val TYPES = FoxVariant.values()

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.FOLLOW_RANGE, 32.0)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
    }
}
