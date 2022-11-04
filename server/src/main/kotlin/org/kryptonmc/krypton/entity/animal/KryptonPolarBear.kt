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

import org.kryptonmc.api.entity.animal.PolarBear
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.Neutral
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.PolarBearSerializer
import org.kryptonmc.krypton.util.provider.UniformInt
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID
import kotlin.random.Random

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
        remainingAngerTime = PERSISTENT_ANGER_TIME.sample(Random)
    }

    override fun isFood(item: ItemStack): Boolean = false

    companion object {

        private val PERSISTENT_ANGER_TIME = UniformInt(20 * 20, 39 * 20)

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, 30.0)
            .add(KryptonAttributeTypes.FOLLOW_RANGE, 20.0)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, 0.25)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, 6.0)
    }
}
