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
import org.kryptonmc.api.entity.animal.Fox
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.toUUID
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntArrayTag
import java.util.UUID

class KryptonFox(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.FOX, ATTRIBUTES), Fox {

    override var foxType: FoxType
        get() = FoxType.fromId(data[MetadataKeys.FOX.TYPE])!!
        set(value) = data.set(MetadataKeys.FOX.TYPE, value.ordinal)
    override var isSitting: Boolean
        get() = getFlag(1)
        set(value) = setFlag(1, value)
    override var isCrouching: Boolean
        get() = getFlag(4)
        set(value) = setFlag(4, value)
    override var isInterested: Boolean
        get() = getFlag(8)
        set(value) = setFlag(8, value)
    override var isPouncing: Boolean
        get() = getFlag(16)
        set(value) = setFlag(16, value)
    override var isSleeping: Boolean
        get() = getFlag(32)
        set(value) = setFlag(32, value)
    override var hasFaceplanted: Boolean
        get() = getFlag(64)
        set(value) = setFlag(64, value)
    override var isDefending: Boolean
        get() = getFlag(128)
        set(value) = setFlag(128, value)
    override var firstTrusted: UUID?
        get() = data[MetadataKeys.FOX.FIRST_TRUSTED]
        set(value) = data.set(MetadataKeys.FOX.FIRST_TRUSTED, value)
    override var secondTrusted: UUID?
        get() = data[MetadataKeys.FOX.SECOND_TRUSTED]
        set(value) = data.set(MetadataKeys.FOX.SECOND_TRUSTED, value)
    override var target: KryptonLivingEntity?
        get() = super.target
        set(value) {
            if (isDefending && value == null) isDefending = false
            super.target = value
        }

    init {
        data.add(MetadataKeys.FOX.FIRST_TRUSTED)
        data.add(MetadataKeys.FOX.SECOND_TRUSTED)
        data.add(MetadataKeys.FOX.TYPE)
        data.add(MetadataKeys.FOX.FLAGS)
    }

    override fun trusts(uuid: UUID): Boolean = uuid == firstTrusted || uuid == secondTrusted

    override fun load(tag: CompoundTag) {
        super.load(tag)
        tag.getList("Trusted", IntArrayTag.ID).forEachIntArray { addTrustedId(it.toUUID()) }
        isSleeping = tag.getBoolean("Sleeping")
        if (tag.contains("Type")) foxType = FoxType.fromName(tag.getString("Type"))!!
        isSitting = tag.getBoolean("Sitting")
        isCrouching = tag.getBoolean("Crouching")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        list("Trusted") {
            if (firstTrusted != null) addUUID(firstTrusted!!)
            if (secondTrusted != null) addUUID(secondTrusted!!)
        }
        boolean("Sleeping", isSleeping)
        string("Type", foxType.serialized)
        boolean("Sitting", isSitting)
        boolean("Crouching", isCrouching)
    }

    override fun isFood(item: ItemStack): Boolean = ItemTags.FOX_FOOD.contains(item.type)

    private fun getFlag(index: Int): Boolean = data[MetadataKeys.FOX.FLAGS].toInt() and index != 0

    private fun setFlag(index: Int, value: Boolean) {
        val old = data[MetadataKeys.FOX.FLAGS].toInt()
        if (value) {
            data[MetadataKeys.FOX.FLAGS] = (old or index).toByte()
        } else {
            data[MetadataKeys.FOX.FLAGS] = (old and index.inv()).toByte()
        }
    }

    private fun addTrustedId(uuid: UUID?) {
        if (data[MetadataKeys.FOX.FIRST_TRUSTED] != null) {
            data[MetadataKeys.FOX.SECOND_TRUSTED] = uuid
        } else {
            data[MetadataKeys.FOX.FIRST_TRUSTED] = uuid
        }
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.FOLLOW_RANGE, 32.0)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
            .build()
    }
}
