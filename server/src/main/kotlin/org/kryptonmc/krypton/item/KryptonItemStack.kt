/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.event.HoverEvent
import org.kryptonmc.api.item.ItemLike
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.krypton.item.meta.AbstractItemMeta
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import java.util.function.UnaryOperator

@JvmRecord
data class KryptonItemStack(override val type: KryptonItemType, override val amount: Int, override val meta: AbstractItemMeta<*>) : ItemStack {

    constructor(like: ItemLike) : this(like.asItem().downcast(), 1, KryptonItemMeta.DEFAULT)

    fun save(tag: CompoundTag.Builder): CompoundTag.Builder = tag.apply {
        putString("id", type.key().asString())
        putInt("Count", amount)
        put("tag", meta.data)
    }

    fun save(): CompoundTag = save(ImmutableCompoundTag.builder()).build()

    fun isEmpty(): Boolean = type === ItemTypes.AIR || amount <= 0

    fun eq(type: ItemType): Boolean = this.type === type

    override fun <I : ItemMeta> meta(type: Class<I>): I? = if (type.isInstance(meta)) type.cast(meta) else null

    override fun withType(type: ItemType): KryptonItemStack {
        if (type == this.type) return this
        return KryptonItemStack(type.downcast(), amount, meta)
    }

    override fun withAmount(amount: Int): KryptonItemStack {
        if (amount == this.amount) return this
        return KryptonItemStack(type, amount, meta)
    }

    override fun grow(amount: Int): KryptonItemStack = withAmount(this.amount + amount)

    override fun shrink(amount: Int): KryptonItemStack = withAmount(this.amount - amount)

    override fun withMeta(meta: ItemMeta): KryptonItemStack {
        if (meta == this.meta) return this
        return KryptonItemStack(type, amount, meta as AbstractItemMeta<*>)
    }

    //override fun withMeta(builder: ItemMeta.Builder.() -> Unit): KryptonItemStack = withMeta(ItemMeta.builder().apply(builder).build())

    override fun toBuilder(): Builder = Builder(this)

    override fun asHoverEvent(op: UnaryOperator<HoverEvent.ShowItem>): HoverEvent<HoverEvent.ShowItem> =
        HoverEvent.showItem(op.apply(HoverEvent.ShowItem.of(type.key(), amount, BinaryTagHolder.binaryTagHolder(meta.data.asString()))))

    class Builder() : ItemStack.Builder {

        private var type: KryptonItemType = ItemTypes.AIR.get().downcast()
        private var amount = 1
        private var meta: AbstractItemMeta<*> = KryptonItemMeta.DEFAULT

        constructor(item: KryptonItemStack) : this() {
            type = item.type
            amount = item.amount
            meta = item.meta
        }

        override fun type(type: ItemType): Builder = apply { this.type = type.downcast() }

        override fun amount(amount: Int): Builder = apply {
            require(amount in 1..type.maximumStackSize) { "Item amount must be between 1 and ${type.maximumStackSize}, was $amount!" }
            this.amount = amount
        }

        override fun meta(meta: ItemMeta): Builder = apply { this.meta = meta.downcastBase() }

        inline fun meta(builder: ItemMeta.Builder.() -> Unit): Builder = meta(KryptonItemMeta.Builder().apply(builder).build())

        @JvmName("metaGeneric")
        inline fun <B : ItemMetaBuilder<B, P>, reified P> meta(builder: B.() -> Unit): Builder where P : ItemMetaBuilder.Provider<B>, P : ItemMeta =
            meta(ItemFactory.builder(P::class.java).apply(builder).build())

        override fun build(): KryptonItemStack {
            if (type == EMPTY.type && amount == EMPTY.amount && meta == EMPTY.meta) return EMPTY
            return KryptonItemStack(type, amount, meta)
        }
    }

    object Factory : ItemStack.Factory {

        override fun builder(): Builder = Builder()

        override fun empty(): ItemStack = EMPTY
    }

    companion object {

        @JvmField
        val EMPTY: KryptonItemStack = KryptonItemStack(ItemTypes.AIR.get())

        @JvmStatic
        fun from(data: CompoundTag): KryptonItemStack {
            val type = KryptonRegistries.ITEM.get(Key.key(data.getString("id")))
            return KryptonItemStack(type, data.getInt("Count"), ItemFactory.create(type, data.getCompound("tag")))
        }
    }
}
