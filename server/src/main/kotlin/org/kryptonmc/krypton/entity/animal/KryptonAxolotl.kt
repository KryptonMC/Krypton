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

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Axolotl
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.BucketStorable
import org.kryptonmc.krypton.entity.memory.MemoryKeys
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonAxolotl(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.AXOLOTL, ATTRIBUTES), Axolotl, BucketStorable {

    override var variant: AxolotlVariant
        get() = AxolotlVariant.fromId(data[MetadataKeys.AXOLOTL.VARIANT])!!
        set(value) = data.set(MetadataKeys.AXOLOTL.VARIANT, value.ordinal)
    override var isPlayingDead: Boolean
        get() = data[MetadataKeys.AXOLOTL.PLAYING_DEAD]
        set(value) = data.set(MetadataKeys.AXOLOTL.PLAYING_DEAD, value)
    override var spawnedFromBucket: Boolean
        get() = data[MetadataKeys.AXOLOTL.FROM_BUCKET]
        set(value) = data.set(MetadataKeys.AXOLOTL.FROM_BUCKET, value)

    override val pickupSound: SoundEvent
        get() = SoundEvents.BUCKET_FILL_AXOLOTL
    override val swimSound: SoundEvent
        get() = SoundEvents.AXOLOTL_SWIM
    override val splashSound: SoundEvent
        get() = SoundEvents.AXOLOTL_SPLASH
    override val bucketItem: KryptonItemStack
        get() = KryptonItemStack(ItemTypes.AXOLOTL_BUCKET, 1, KryptonItemMeta.DEFAULT)
    override val maxAirTicks: Int
        get() = 6000
    override val pushedByFluid: Boolean
        get() = false
    override val canBeSeenAsEnemy: Boolean
        get() = !isPlayingDead && super.canBeSeenAsEnemy

    init {
        data.add(MetadataKeys.AXOLOTL.VARIANT)
        data.add(MetadataKeys.AXOLOTL.PLAYING_DEAD)
        data.add(MetadataKeys.AXOLOTL.FROM_BUCKET)
    }

    override fun isFood(item: ItemStack): Boolean = ItemTags.AXOLOTL_TEMPT_ITEMS.contains(item.type)

    override fun loadFromBucket(tag: CompoundTag) {
        loadDefaultsFromBucket(this, tag)
        data[MetadataKeys.AXOLOTL.VARIANT] = tag.getInt("Variant")
        if (tag.contains("Age")) age = tag.getInt("Age")
        if (tag.contains("HuntingCooldown")) {
            brain.set(MemoryKeys.HAS_HUNTING_COOLDOWN, true, tag.getLong("HuntingCooldown"))
        }
    }

    /* FIXME
    override fun saveToBucket(item: KryptonItemStack) {
        saveDefaultsToBucket(this, item)
        item.meta.nbt.apply {
            putInt("Variant", data[MetadataKeys.AXOLOTL.VARIANT])
            putInt("Age", age)
            if (brain.contains(MemoryKeys.HAS_HUNTING_COOLDOWN)) {
                putLong("HuntingCooldown", brain.expiryTime(MemoryKeys.HAS_HUNTING_COOLDOWN))
            }
        }
    }
     */

    override fun load(tag: CompoundTag) {
        super.load(tag)
        data[MetadataKeys.AXOLOTL.VARIANT] = tag.getInt("Variant")
        spawnedFromBucket = tag.getBoolean("FromBucket")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("Variant", data[MetadataKeys.AXOLOTL.VARIANT])
        boolean("FromBucket", spawnedFromBucket)
    }

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 14.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 1.0)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
            .build()
    }
}
