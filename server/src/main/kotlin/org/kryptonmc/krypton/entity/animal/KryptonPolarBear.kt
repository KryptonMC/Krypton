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

import org.kryptonmc.api.entity.animal.PolarBear
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.components.Neutral
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.PolarBearSerializer
import org.kryptonmc.krypton.util.provider.UniformInt
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

class KryptonPolarBear(world: KryptonWorld) : KryptonAnimal(world), PolarBear, Neutral {

    override val type: KryptonEntityType<KryptonPolarBear>
        get() = KryptonEntityTypes.POLAR_BEAR
    override val serializer: EntitySerializer<KryptonPolarBear>
        get() = PolarBearSerializer

    override var remainingAngerTime: Int = 0
    override var angerTarget: UUID? = null

    override var isStanding: Boolean
        get() = data.get(MetadataKeys.PolarBear.STANDING)
        set(value) = data.set(MetadataKeys.PolarBear.STANDING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.PolarBear.STANDING, false)
    }

    override fun startAngerTimer() {
        remainingAngerTime = PERSISTENT_ANGER_TIME.sample(random)
    }

    override fun isFood(item: ItemStack): Boolean = false

    companion object {

        private val PERSISTENT_ANGER_TIME = UniformInt(20 * 20, 39 * 20)
        private const val DEFAULT_MAX_HEALTH = 30.0
        private const val DEFAULT_FOLLOW_RANGE = 20.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.25
        private const val DEFAULT_ATTACK_DAMAGE = 6.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.FOLLOW_RANGE, DEFAULT_FOLLOW_RANGE)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
    }
}
