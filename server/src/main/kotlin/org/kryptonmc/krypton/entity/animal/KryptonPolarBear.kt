/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.entity.animal.PolarBear
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.entity.Neutral
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.sample
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID
import kotlin.random.Random

class KryptonPolarBear(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.POLAR_BEAR, ATTRIBUTES), PolarBear, Neutral {

    override var remainingAngerTime: Int = 0
    override var angerTarget: UUID? = null

    override var isStanding: Boolean
        get() = data[MetadataKeys.POLAR_BEAR.STANDING]
        set(value) = data.set(MetadataKeys.POLAR_BEAR.STANDING, value)

    init {
        data.add(MetadataKeys.POLAR_BEAR.STANDING)
    }

    override fun startAngerTimer() {
        remainingAngerTime = PERSISTENT_ANGER_TIME.sample(Random)
    }

    override fun isFood(item: ItemStack): Boolean = false

    override fun load(tag: CompoundTag) {
        super.load(tag)
        loadAngerData(world, tag)
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        saveAngerData(this)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 30.0)
            .add(AttributeTypes.FOLLOW_RANGE, 20.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.25)
            .add(AttributeTypes.ATTACK_DAMAGE, 6.0)
            .build()
        private val PERSISTENT_ANGER_TIME = 20..39
    }
}
