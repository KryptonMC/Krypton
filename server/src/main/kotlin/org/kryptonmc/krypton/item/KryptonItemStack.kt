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
package org.kryptonmc.krypton.item

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.MetaHolder
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.toKey
import org.kryptonmc.krypton.item.meta.KryptonMetaHolder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag

open class KryptonItemStack(
    override val type: ItemType,
    override var amount: Int,
    override val meta: KryptonMetaHolder = KryptonMetaHolder()
) : ItemStack {

    constructor(nbt: CompoundTag) : this(
        Registries.ITEM[nbt.getString("id").toKey()],
        nbt.getInt("Count"),
        KryptonMetaHolder(nbt.getCompound("tag").let { if (it is MutableCompoundTag) it else it.mutable() })
    )

    fun getOrCreateTag(key: String): CompoundTag {
        if (meta.nbt.contains(key, CompoundTag.ID)) return meta.nbt.getCompound(key)
        return CompoundTag().apply { meta.nbt[key] = this }
    }

    fun save(tag: MutableCompoundTag) = tag
        .putString("id", Registries.ITEM[type].asString())
        .putInt("Count", amount)
        .apply { put("tag", meta.nbt) }

    fun isEmpty(): Boolean {
        if (this === EmptyItemStack) return true
        if (type !== ItemTypes.AIR) return amount <= 0
        return true
    }

    fun getDestroySpeed(block: Block) = type.handler.getDestroySpeed(this, block)

    override fun copy(): KryptonItemStack {
        if (isEmpty()) return EmptyItemStack
        return KryptonItemStack(type, amount, meta.copy())
    }

    override fun toBuilder() = Builder()

    class Builder(
        private var type: ItemType = ItemTypes.AIR,
        private var amount: Int = 1,
        private var meta: KryptonMetaHolder = KryptonMetaHolder()
    ) : ItemStack.Builder {

        override fun type(type: ItemType) = apply { this.type = type }

        override fun amount(amount: Int) = apply {
            require(amount in 1..type.maximumAmount) { "Item amount must be between 1 and ${type.maximumAmount}, was $amount!" }
            this.amount = amount
        }

        override fun meta(builder: MetaHolder.() -> Unit) = apply { meta.apply(builder) }

        override fun build() = KryptonItemStack(type, amount, meta)
    }
}
