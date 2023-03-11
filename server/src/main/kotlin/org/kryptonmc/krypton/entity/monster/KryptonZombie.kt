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
package org.kryptonmc.krypton.entity.monster

import org.kryptonmc.api.entity.monster.Zombie
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.monster.ZombieSerializer
import org.kryptonmc.krypton.world.KryptonWorld

open class KryptonZombie(world: KryptonWorld) : KryptonMonster(world), Zombie {

    override val type: KryptonEntityType<KryptonZombie>
        get() = KryptonEntityTypes.ZOMBIE
    override val serializer: EntitySerializer<KryptonZombie>
        get() = ZombieSerializer

    internal var conversionTime = 0

    override var isBaby: Boolean
        get() = data.get(MetadataKeys.Zombie.BABY)
        set(value) = data.set(MetadataKeys.Zombie.BABY, value)
    override var isConverting: Boolean
        get() = data.get(MetadataKeys.Zombie.CONVERTING)
        set(value) = data.set(MetadataKeys.Zombie.CONVERTING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Zombie.BABY, false)
        data.define(MetadataKeys.Zombie.CONVERTING, false)
    }

    companion object {

        private const val DEFAULT_FOLLOW_RANGE = 35.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.23
        private const val DEFAULT_ATTACK_DAMAGE = 3.0
        private const val DEFAULT_ARMOR = 2.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes()
            .add(KryptonAttributeTypes.FOLLOW_RANGE, DEFAULT_FOLLOW_RANGE)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
            .add(KryptonAttributeTypes.ARMOR, DEFAULT_ARMOR)
            .add(KryptonAttributeTypes.SPAWN_REINFORCEMENTS_CHANCE)
    }
}
