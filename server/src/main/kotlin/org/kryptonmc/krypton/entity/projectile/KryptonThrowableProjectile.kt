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

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.projectile.ThrowableProjectile
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

abstract class KryptonThrowableProjectile(world: KryptonWorld, type: EntityType<out ThrowableProjectile>) : KryptonProjectile(world, type), ThrowableProjectile {

    abstract val defaultItem: ItemType

    private var rawItem: KryptonItemStack
        get() = data[MetadataKeys.THROWABLE_PROJECTILE.ITEM]
        set(value) {
            if (value.type === defaultItem && value.meta.nbt.isEmpty()) return
            data[MetadataKeys.THROWABLE_PROJECTILE.ITEM] = value.copy().apply { amount = 1 }
        }
    override val item: ItemStack
        get() {
            val raw = rawItem
            return if (raw.isEmpty()) KryptonItemStack(defaultItem, 1) else raw
        }

    init {
        data.add(MetadataKeys.THROWABLE_PROJECTILE.ITEM)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        val item = KryptonItemStack(tag.getCompound("Item"))
        rawItem = item
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        val raw = rawItem
        if (!raw.isEmpty()) put("Item", raw.save(CompoundTag.builder()).build())
    }
}
