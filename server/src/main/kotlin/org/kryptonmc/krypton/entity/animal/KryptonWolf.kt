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
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.Wolf
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.Neutral
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.randomValue
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID
import kotlin.random.Random

class KryptonWolf(world: KryptonWorld) : KryptonTamable(world, EntityTypes.WOLF, ATTRIBUTES), Wolf, Neutral {

    override var angerTarget: UUID? = null
    override var isAngry: Boolean
        get() = remainingAngerTime > 0
        set(value) {
            if (value) {
                startAngerTimer()
            } else {
                stopBeingAngry()
            }
        }

    override var collarColor: DyeColor
        get() = Registries.DYE_COLORS[data[MetadataKeys.WOLF.COLLAR_COLOR]]!!
        set(value) = data.set(MetadataKeys.WOLF.COLLAR_COLOR, Registries.DYE_COLORS.idOf(value))
    override var isBeggingForFood: Boolean
        get() = data[MetadataKeys.WOLF.BEGGING]
        set(value) = data.set(MetadataKeys.WOLF.BEGGING, value)
    override var remainingAngerTime: Int
        get() = data[MetadataKeys.WOLF.ANGER_TIME]
        set(value) = data.set(MetadataKeys.WOLF.ANGER_TIME, value)

    override var isTame: Boolean
        get() = super.isTame
        set(value) {
            super.isTame = value
            if (value) {
                attribute(AttributeTypes.MAX_HEALTH)?.baseValue = 20.0
                health = 20F
            } else {
                attribute(AttributeTypes.MAX_HEALTH)?.baseValue = 8.0
            }
            attribute(AttributeTypes.ATTACK_DAMAGE)?.baseValue = 4.0
        }

    override val soundVolume: Float
        get() = 0.4F

    init {
        data.add(MetadataKeys.WOLF.BEGGING)
        data.add(MetadataKeys.WOLF.COLLAR_COLOR)
        data.add(MetadataKeys.WOLF.ANGER_TIME)
        isTame = false
    }

    override fun startAngerTimer() {
        remainingAngerTime = PERSISTENT_ANGER_TIME.randomValue(Random)
    }

    // TODO: Check if item type is meat
    override fun isFood(item: ItemStack): Boolean = item.type.isEdible

    override fun canMate(target: Animal): Boolean {
        if (target === this) return false
        if (!isTame) return false
        if (target !is Wolf) return false
        if (!target.isTame) return false
        if (target.isSitting) return false
        return inLove && target.inLove
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("CollarColor", 99)) {
            data[MetadataKeys.WOLF.COLLAR_COLOR] = tag.getInt("CollarColor")
        }
        loadAngerData(world, tag)
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        byte("CollarColor", data[MetadataKeys.WOLF.COLLAR_COLOR].toByte())
        saveAngerData(this)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.MAX_HEALTH, 8.0)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
            .build()
        private val PERSISTENT_ANGER_TIME = 20..39
    }
}
