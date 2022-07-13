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
import org.kryptonmc.api.entity.animal.Bee
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.Neutral
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.sample
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i
import java.util.UUID
import kotlin.random.Random

class KryptonBee(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.BEE, ATTRIBUTES), Bee, Neutral {

    override var cannotEnterHiveTicks: Int = 0
    override var hive: Vector3i? = null
    override var flower: Vector3i? = null
    override var angerTarget: UUID? = null
    internal var timeSincePollination = 0
    internal var cropsGrownSincePollination = 0

    override var isAngry: Boolean
        get() = getFlag(1)
        set(value) = setFlag(1, value)
    override var hasStung: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)
    override var hasNectar: Boolean
        get() = getFlag(3)
        set(value) {
            if (value) timeSincePollination = 0
            setFlag(3, value)
        }
    override var remainingAngerTime: Int
        get() = data[MetadataKeys.BEE.ANGER_TIME]
        set(value) = data.set(MetadataKeys.BEE.ANGER_TIME, value)

    override val soundVolume: Float
        get() = 0.4F

    init {
        data.add(MetadataKeys.BEE.FLAGS, 0)
        data.add(MetadataKeys.BEE.ANGER_TIME, 0)
    }

    override fun startAngerTimer() {
        remainingAngerTime = PERSISTENT_ANGER_TIME.sample(Random)
    }

    override fun isFood(item: ItemStack): Boolean = ItemTags.FLOWERS.contains(item.type)

    private fun getFlag(flag: Int): Boolean = data.getFlag(MetadataKeys.BEE.FLAGS, flag)

    private fun setFlag(flag: Int, state: Boolean) {
        data.setFlag(MetadataKeys.BEE.FLAGS, flag, state)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.FLYING_SPEED, 0.6)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
            .add(AttributeTypes.FOLLOW_RANGE, 48.0)
            .build()
        private val PERSISTENT_ANGER_TIME = 20..39
    }
}
