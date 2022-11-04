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
package org.kryptonmc.krypton.item.meta

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.util.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonBundleMeta(data: CompoundTag) : AbstractItemMeta<KryptonBundleMeta>(data), BundleMeta {

    override val items: ImmutableList<ItemStack> = data.mapToList("Items", CompoundTag.ID) { KryptonItemStack.from(it as CompoundTag) }

    override fun copy(data: CompoundTag): KryptonBundleMeta = KryptonBundleMeta(data)

    override fun withItems(items: List<ItemStack>): KryptonBundleMeta = copy(data.setItems(items))

    override fun withItem(item: ItemStack): KryptonBundleMeta = copy(data.update("Items", CompoundTag.ID) { it.add(item.downcast().save()) })

    override fun withoutItem(index: Int): KryptonBundleMeta = copy(data.update("Items", CompoundTag.ID) { it.remove(index) })

    override fun withoutItem(item: ItemStack): KryptonBundleMeta = copy(data.update("Items", CompoundTag.ID) { it.remove(item.downcast().save()) })

    override fun toBuilder(): BundleMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonBundleMeta(${partialToString()}, items=$items)"

    class Builder : KryptonItemMetaBuilder<BundleMeta.Builder, BundleMeta>, BundleMeta.Builder {

        private var items: MutableCollection<ItemStack>

        constructor() : super() {
            items = BuilderCollection()
        }

        constructor(meta: KryptonBundleMeta) : super(meta) {
            items = BuilderCollection(meta.items)
        }

        override fun items(items: Collection<ItemStack>): Builder = apply { this.items = BuilderCollection(items) }

        override fun addItem(item: ItemStack): Builder = apply { items.add(item) }

        override fun build(): BundleMeta = KryptonBundleMeta(buildData().build())

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (items.isNotEmpty()) list("Items") { items.forEach { it.downcast().save() } }
        }
    }
}

private fun CompoundTag.setItems(items: List<ItemStack>): CompoundTag {
    if (items.isEmpty()) return remove("Items")
    return put("Items", list { items.forEach { it.downcast().save() } })
}
