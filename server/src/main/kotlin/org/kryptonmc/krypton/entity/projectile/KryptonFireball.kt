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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.projectile.Fireball
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

abstract class KryptonFireball(world: KryptonWorld, type: EntityType<out Fireball>) : KryptonAcceleratingProjectile(world, type), Fireball {

    private var item: KryptonItemStack
        get() = data[MetadataKeys.FIREBALL.ITEM]
        set(value) {
            if (value.type === ItemTypes.FIRE_CHARGE && value.meta == KryptonItemMeta.DEFAULT) return
            data[MetadataKeys.FIREBALL.ITEM] = value.withAmount(1)
        }

    init {
        data.add(MetadataKeys.FIREBALL.ITEM)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        item = KryptonItemStack(tag.getCompound("Item"))
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        val item = item
        if (!item.isEmpty()) put("Item", item.save(CompoundTag.builder()).build())
    }

    final override fun asItem(): ItemStack {
        val item = item
        if (item.isEmpty()) return DEFAULT_ITEM
        return item
    }

    companion object {

        private val DEFAULT_ITEM = KryptonItemStack(ItemTypes.FIRE_CHARGE, 1, KryptonItemMeta.DEFAULT)
    }
}
