/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.animal.Fox
import org.kryptonmc.api.entity.animal.type.FoxVariant
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.FoxSerializer
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

class KryptonFox(world: KryptonWorld) : KryptonAnimal(world), Fox {

    override val type: KryptonEntityType<KryptonFox>
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

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Fox.TYPE, FoxVariant.RED.ordinal)
        data.define(MetadataKeys.Fox.FLAGS, 0)
        data.define(MetadataKeys.Fox.FIRST_TRUSTED, null)
        data.define(MetadataKeys.Fox.SECOND_TRUSTED, null)
    }

    override fun trusts(uuid: UUID): Boolean = uuid == firstTrusted || uuid == secondTrusted

    override fun isFood(item: ItemStack): Boolean = item.type.downcast().eq(ItemTags.FOX_FOOD)

    override fun setTarget(target: KryptonLivingEntity?) {
        if (isDefending && target == null) isDefending = false
        super.setTarget(target)
    }

    companion object {

        private const val FLAG_SITTING = 0
        private const val FLAG_CROUCHING = 2
        private const val FLAG_INTERESTED = 3
        private const val FLAG_POUNCING = 4
        private const val FLAG_SLEEPING = 5
        private const val FLAG_FACEPLANTED = 6
        private const val FLAG_DEFENDING = 7
        private val TYPES = FoxVariant.values()

        private const val DEFAULT_MOVEMENT_SPEED = 0.3
        private const val DEFAULT_MAX_HEALTH = 10.0
        private const val DEFAULT_FOLLOW_RANGE = 32.0
        private const val DEFAULT_ATTACK_DAMAGE = 2.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.FOLLOW_RANGE, DEFAULT_FOLLOW_RANGE)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
    }
}
