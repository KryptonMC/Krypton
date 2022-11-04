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

class KryptonZombie(world: KryptonWorld) : KryptonMonster(world), Zombie {

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

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes()
            .add(KryptonAttributeTypes.FOLLOW_RANGE, 35.0)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, 0.23)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, 3.0)
            .add(KryptonAttributeTypes.ARMOR, 2.0)
            .add(KryptonAttributeTypes.SPAWN_REINFORCEMENTS_CHANCE)
    }
}
