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

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.attribute.AttributeMap
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.Attributes
import org.kryptonmc.krypton.entity.attribute.attributeSupplier
import org.kryptonmc.krypton.entity.metadata.EntityDataSerializers
import org.kryptonmc.krypton.entity.metadata.EntityData
import org.spongepowered.math.vector.Vector3i
import java.util.Optional
import java.util.UUID

abstract class KryptonLivingEntity(
    id: Int,
    server: KryptonServer,
    uuid: UUID,
    type: EntityType<out LivingEntity>
) : KryptonEntity(id, server, uuid, type), LivingEntity {

    override var absorption = 0F
    val attributes = AttributeMap(type.attributeSupplier)

    init {
        println("Attributes for type ${type.key} is ${attributes.syncableAttributes}, supplier is ${type.attributeSupplier}")
    }

    override fun defineExtraData() {
        data.define(DATA_LIVING_ENTITY_FLAGS, 0)
        data.define(DATA_EFFECT_COLOR_ID, 0)
        data.define(DATA_EFFECT_AMBIENCE_ID, false)
        data.define(DATA_ARROW_COUNT_ID, 0)
        data.define(DATA_STINGER_COUNT_ID, 0)
        data.define(DATA_HEALTH_ID, 1F)
        data.define(SLEEPING_POS_ID, Optional.empty())
    }

    private fun setLivingSharedFlag(flag: Int, state: Boolean) {
        val flags = data[DATA_LIVING_ENTITY_FLAGS].toInt()
        data[DATA_LIVING_ENTITY_FLAGS] = (if (state) flags or flag else flags and flag.inv()).toByte()
    }

    override var isUsingItem: Boolean
        get() = data[DATA_LIVING_ENTITY_FLAGS].toInt() and 1 > 0
        set(value) = setLivingSharedFlag(1, value)

    override var hand: Hand
        get() = if (data[DATA_LIVING_ENTITY_FLAGS].toInt() and 2 > 0) Hand.OFF else Hand.MAIN
        set(value) = setLivingSharedFlag(2, value == Hand.OFF)

    override var isAutoSpinAttack: Boolean
        get() = data[DATA_LIVING_ENTITY_FLAGS].toInt() and 4 > 0
        set(value) = setLivingSharedFlag(4, value)

    override var health: Float
        get() = data[DATA_HEALTH_ID]
        set(value) = data.set(DATA_HEALTH_ID, value)

    var potionEffectColor: Int
        get() = data[DATA_EFFECT_COLOR_ID]
        set(value) = data.set(DATA_EFFECT_COLOR_ID, value)

    var isPotionEffectAmbient: Boolean
        get() = data[DATA_EFFECT_AMBIENCE_ID]
        set(value) = data.set(DATA_EFFECT_AMBIENCE_ID, value)

    var arrowCount: Int
        get() = data[DATA_ARROW_COUNT_ID]
        set(value) = data.set(DATA_ARROW_COUNT_ID, value)

    var stingerCount: Int
        get() = data[DATA_STINGER_COUNT_ID]
        set(value) = data.set(DATA_STINGER_COUNT_ID, value)

    var bedPosition: Optional<Vector3i>
        get() = data[SLEEPING_POS_ID]
        set(value) = data.set(SLEEPING_POS_ID, value)

    companion object {

        private val DATA_LIVING_ENTITY_FLAGS = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.BYTE)
        private val DATA_HEALTH_ID = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.FLOAT)
        private val DATA_EFFECT_COLOR_ID = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.INT)
        private val DATA_EFFECT_AMBIENCE_ID = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.BOOLEAN)
        private val DATA_ARROW_COUNT_ID = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.INT)
        private val DATA_STINGER_COUNT_ID = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.INT)
        private val SLEEPING_POS_ID = EntityData.define(KryptonLivingEntity::class, EntityDataSerializers.OPTIONAL_BLOCK_POS)

        fun createAttributes() = AttributeSupplier.builder()
            .add(Attributes.MAX_HEALTH)
            .add(Attributes.KNOCKBACK_RESISTANCE)
            .add(Attributes.MOVEMENT_SPEED)
            .add(Attributes.ARMOR)
            .add(Attributes.ARMOR_TOUGHNESS)
    }
}
