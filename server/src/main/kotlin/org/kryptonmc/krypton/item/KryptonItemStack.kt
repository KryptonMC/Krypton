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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.MetaHolder
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.meta.KryptonMetaHolder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag

open class KryptonItemStack(
    override val type: ItemType,
    override var amount: Int,
    override val meta: KryptonMetaHolder = KryptonMetaHolder()
) : ItemStack {

    constructor(nbt: CompoundTag) : this(
        Registries.ITEM[Key.key(nbt.getString("id"))],
        nbt.getInt("Count"),
        KryptonMetaHolder(nbt.getCompound("tag").mutable())
    )

    fun getOrCreateTag(key: String): MutableCompoundTag {
        if (meta.nbt.contains(key, CompoundTag.ID)) return meta.nbt.getCompound(key).mutable()
        return MutableCompoundTag().apply { meta.nbt.put(key, this) }
    }

    fun save(tag: CompoundTag.Builder): CompoundTag.Builder = tag.apply {
        string("id", type.key().asString())
        int("Count", amount)
        put("tag", meta.nbt)
    }

    fun isEmpty(): Boolean {
        if (this === EmptyItemStack) return true
        if (type !== ItemTypes.AIR) return amount <= 0
        return true
    }

    override fun copy(): KryptonItemStack {
        if (isEmpty()) return EmptyItemStack
        return KryptonItemStack(type, amount, meta.copy())
    }

    override fun toBuilder(): Builder = Builder()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return type == (other as KryptonItemStack).type && amount == other.amount && meta == other.meta
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + type.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + meta.hashCode()
        return result
    }

    override fun toString(): String = "KryptonItemStack(type=$type, amount=$amount, meta=$meta)"

    class Builder(
        private var type: ItemType = ItemTypes.AIR,
        private var amount: Int = 1,
        private var meta: KryptonMetaHolder = KryptonMetaHolder()
    ) : ItemStack.Builder {

        override fun type(type: ItemType): ItemStack.Builder = apply { this.type = type }

        override fun amount(amount: Int): ItemStack.Builder = apply {
            require(amount in 1..type.maximumStackSize) {
                "Item amount must be between 1 and ${type.maximumStackSize}, was $amount!"
            }
            this.amount = amount
        }

        override fun meta(builder: MetaHolder.() -> Unit): ItemStack.Builder = apply { meta.apply(builder) }

        override fun build(): KryptonItemStack = KryptonItemStack(type, amount, meta)
    }

    object Factory : ItemStack.Factory {

        override fun builder(): Builder = Builder()

        override fun empty(): EmptyItemStack = EmptyItemStack
    }
}
