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
package org.kryptonmc.krypton.entity

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.krypton.entity.attribute.AttributeMap
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.Attributes
import org.kryptonmc.krypton.entity.attribute.attributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.nbt.contains
import org.kryptonmc.krypton.util.nbt.getBoolean
import org.kryptonmc.krypton.util.nbt.getFloat
import org.kryptonmc.krypton.util.nbt.getInt
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i
import java.util.Optional

abstract class KryptonLivingEntity(
    world: KryptonWorld,
    type: EntityType<out LivingEntity>
) : KryptonEntity(world, type), LivingEntity {

    override var absorption = 0F
    val attributes = AttributeMap(type.attributeSupplier)

    init {
        data += MetadataKeys.LIVING.FLAGS
        data += MetadataKeys.LIVING.HEALTH
        data += MetadataKeys.LIVING.POTION_EFFECT_COLOR
        data += MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE
        data += MetadataKeys.LIVING.ARROWS
        data += MetadataKeys.LIVING.STINGERS
        data += MetadataKeys.LIVING.BED_LOCATION
    }

    override fun load(tag: NBTCompound) {
        super.load(tag)
        absorption = tag.getFloat("AbsorptionAmount", 0F)
        if (tag.contains("Attributes", 9)) attributes.load(tag.getList("Attributes")!!)
        if (tag.contains("Health", 99)) health = tag.getFloat("Health", 0F)
        if (tag.getBoolean("FallFlying", false)) isFlying = true
        if (tag.contains("SleepingX", 99) && tag.contains("SleepingY", 99) && tag.contains("SleepingZ", 99)) {
            val position = Vector3i(tag.getInt("SleepingX", 0), tag.getInt("SleepingY", 0), tag.getInt("SleepingZ", 0))
            bedPosition = Optional.of(position)
            pose = Pose.SLEEPING
        }
    }

    private fun setLivingFlag(flag: Int, state: Boolean) {
        val flags = data[MetadataKeys.LIVING.FLAGS].toInt()
        data[MetadataKeys.LIVING.FLAGS] = (if (state) flags or flag else flags and flag.inv()).toByte()
    }

    override var isUsingItem: Boolean
        get() = data[MetadataKeys.LIVING.FLAGS].toInt() and 1 > 0
        set(value) = setLivingFlag(1, value)

    override var hand: Hand
        get() = if (data[MetadataKeys.LIVING.FLAGS].toInt() and 2 > 0) Hand.OFF else Hand.MAIN
        set(value) = setLivingFlag(2, value == Hand.OFF)

    override var inSpinAttack: Boolean
        get() = data[MetadataKeys.LIVING.FLAGS].toInt() and 4 > 0
        set(value) = setLivingFlag(4, value)

    override var health: Float
        get() = data[MetadataKeys.LIVING.HEALTH]
        set(value) = data.set(MetadataKeys.LIVING.HEALTH, value)

    var potionEffectColor: Int
        get() = data[MetadataKeys.LIVING.POTION_EFFECT_COLOR]
        set(value) = data.set(MetadataKeys.LIVING.POTION_EFFECT_COLOR, value)

    var isPotionEffectAmbient: Boolean
        get() = data[MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE]
        set(value) = data.set(MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE, value)

    var arrowCount: Int
        get() = data[MetadataKeys.LIVING.ARROWS]
        set(value) = data.set(MetadataKeys.LIVING.ARROWS, value)

    var stingerCount: Int
        get() = data[MetadataKeys.LIVING.STINGERS]
        set(value) = data.set(MetadataKeys.LIVING.STINGERS, value)

    var bedPosition: Optional<Vector3i>
        get() = data[MetadataKeys.LIVING.BED_LOCATION]
        set(value) = data.set(MetadataKeys.LIVING.BED_LOCATION, value)

    companion object {

        fun createAttributes() = AttributeSupplier.builder()
            .add(Attributes.MAX_HEALTH)
            .add(Attributes.KNOCKBACK_RESISTANCE)
            .add(Attributes.MOVEMENT_SPEED)
            .add(Attributes.ARMOR)
            .add(Attributes.ARMOR_TOUGHNESS)
    }
}
