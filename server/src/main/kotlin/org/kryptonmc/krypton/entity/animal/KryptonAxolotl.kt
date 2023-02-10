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
import org.kryptonmc.api.entity.animal.Axolotl
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.entity.components.BucketStorable
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.ai.memory.MemoryKeys
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.AxolotlSerializer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonAxolotl(world: KryptonWorld) : KryptonAnimal(world), Axolotl, BucketStorable {

    override val type: KryptonEntityType<KryptonAxolotl>
        get() = KryptonEntityTypes.AXOLOTL
    override val serializer: EntitySerializer<KryptonAxolotl>
        get() = AxolotlSerializer

    override var variant: AxolotlVariant
        get() = VARIANTS.getOrNull(data.get(MetadataKeys.Axolotl.VARIANT)) ?: AxolotlVariant.LUCY
        set(value) = data.set(MetadataKeys.Axolotl.VARIANT, value.ordinal)
    override var isPlayingDead: Boolean
        get() = data.get(MetadataKeys.Axolotl.PLAYING_DEAD)
        set(value) = data.set(MetadataKeys.Axolotl.PLAYING_DEAD, value)

    override val bucketType: ItemType
        get() = ItemTypes.AXOLOTL_BUCKET.get()
    override val bucketPickupSound: SoundEvent
        get() = SoundEvents.BUCKET_FILL_AXOLOTL.get()

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Axolotl.VARIANT, AxolotlVariant.LUCY.ordinal)
        data.define(MetadataKeys.Axolotl.PLAYING_DEAD, false)
        data.define(MetadataKeys.Axolotl.FROM_BUCKET, false)
    }

    override fun wasSpawnedFromBucket(): Boolean = data.get(MetadataKeys.Axolotl.FROM_BUCKET)

    override fun setSpawnedFromBucket(value: Boolean) {
        data.set(MetadataKeys.Axolotl.FROM_BUCKET, value)
    }

    override fun isFood(item: ItemStack): Boolean = item.type.downcast().eq(ItemTags.AXOLOTL_TEMPT_ITEMS)

    override fun loadFromBucket(tag: CompoundTag) {
        BucketStorable.loadDefaultsFromBucket(this, tag)
        data.set(MetadataKeys.Axolotl.VARIANT, tag.getInt("Variant"))
        if (tag.contains("Age")) age = tag.getInt("Age")
        if (tag.contains("HuntingCooldown")) brain.setMemory(MemoryKeys.HAS_HUNTING_COOLDOWN, true, tag.getLong("HuntingCooldown"))
    }

    /* FIXME
    override fun saveToBucket(item: KryptonItemStack) {
        saveDefaultsToBucket(this, item)
        item.meta.nbt.apply {
            putInt("Variant", data.get(MetadataKeys.AXOLOTL.VARIANT))
            putInt("Age", age)
            if (brain.contains(MemoryKeys.HAS_HUNTING_COOLDOWN)) {
                putLong("HuntingCooldown", brain.expiryTime(MemoryKeys.HAS_HUNTING_COOLDOWN))
            }
        }
    }
     */

    // TODO: Implement proper bucket item meta
    override fun asBucket(): KryptonItemStack = KryptonItemStack.EMPTY

    override fun bucket(): KryptonItemStack {
        remove()
        return asBucket()
    }

    override fun isPushedByFluid(): Boolean = false

    override fun maxAirTicks(): Int = MAX_AIR_TICKS

    override fun canBeSeenAsEnemy(): Boolean = !isPlayingDead && super.canBeSeenAsEnemy()

    companion object {

        private const val MAX_AIR_TICKS = 5 * 60 * 20 // 5 minutes in ticks
        private val VARIANTS = AxolotlVariant.values()

        private const val DEFAULT_MAX_HEALTH = 14.0
        private const val DEFAULT_MOVEMENT_SPEED = 1.0
        private const val DEFAULT_ATTACK_DAMAGE = 2.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
    }
}
