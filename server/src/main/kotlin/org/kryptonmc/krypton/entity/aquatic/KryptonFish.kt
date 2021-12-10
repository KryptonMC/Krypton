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
package org.kryptonmc.krypton.entity.aquatic

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.aquatic.Fish
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.BucketStorable
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

abstract class KryptonFish(world: KryptonWorld, type: EntityType<out Fish>) : KryptonAquaticAnimal(world, type, ATTRIBUTES), Fish, BucketStorable {

    override var spawnedFromBucket: Boolean
        get() = data[MetadataKeys.FISH.FROM_BUCKET]
        set(value) = data.set(MetadataKeys.FISH.FROM_BUCKET, value)
    override val bucketItem: KryptonItemStack
        get() = KryptonItemStack(ItemTypes.AXOLOTL_BUCKET, 1)
    override val pickupSound: SoundEvent
        get() = SoundEvents.BUCKET_FILL_FISH
    override val swimSound: SoundEvent
        get() = SoundEvents.FISH_SWIM

    init {
        data.add(MetadataKeys.FISH.FROM_BUCKET)
    }

    override fun loadFromBucket(tag: CompoundTag) {
        loadDefaultsFromBucket(this, tag)
    }

    override fun saveToBucket(item: KryptonItemStack) {
        saveDefaultsToBucket(this, item)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        spawnedFromBucket = tag.getBoolean("FromBucket")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("FromBucket", spawnedFromBucket)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 3.0)
            .build()
    }
}
