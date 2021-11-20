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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.projectile.Trident
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonTrident(world: KryptonWorld) : KryptonArrowLike(world, EntityTypes.TRIDENT, SoundEvents.TRIDENT_HIT_GROUND), Trident {

    override var item = KryptonItemStack(ItemTypes.TRIDENT, 1)
    override var dealtDamage = false

    override var loyaltyLevel: Int
        get() = data[MetadataKeys.TRIDENT.LOYALTY_LEVEL]
        set(value) = data.set(MetadataKeys.TRIDENT.LOYALTY_LEVEL, value)
    override var isEnchanted: Boolean
        get() = data[MetadataKeys.TRIDENT.ENCHANTED]
        set(value) = data.set(MetadataKeys.TRIDENT.ENCHANTED, value)

    init {
        data.add(MetadataKeys.TRIDENT.LOYALTY_LEVEL)
        data.add(MetadataKeys.TRIDENT.ENCHANTED)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        dealtDamage = tag.getBoolean("DealtDamage")
        if (tag.contains("Trident", CompoundTag.ID)) item = KryptonItemStack(tag.getCompound("Trident"))
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("DealtDamage", dealtDamage)
        put("Trident", item.save(CompoundTag.builder()).build())
    }
}
