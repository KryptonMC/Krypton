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

import org.kryptonmc.api.entity.animal.Goat
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.GoatSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonGoat(world: KryptonWorld) : KryptonAnimal(world), Goat {

    override val type: KryptonEntityType<KryptonGoat>
        get() = KryptonEntityTypes.GOAT
    override val serializer: EntitySerializer<KryptonGoat>
        get() = GoatSerializer

    override var canScream: Boolean
        get() = data.get(MetadataKeys.Goat.SCREAMING)
        set(value) = data.set(MetadataKeys.Goat.SCREAMING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Goat.SCREAMING, false)
    }

    override fun onAgeTransformation() {
        getAttribute(KryptonAttributeTypes.ATTACK_DAMAGE)?.baseValue = if (isBaby) BABY_ATTACK_DAMAGE else ADULT_ATTACK_DAMAGE
    }

    companion object {

        private const val BABY_ATTACK_DAMAGE = 1.0
        private const val ADULT_ATTACK_DAMAGE = 2.0

        private const val DEFAULT_MAX_HEALTH = 10.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.2
        private const val DEFAULT_ATTACK_DAMAGE = 2.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
    }
}
