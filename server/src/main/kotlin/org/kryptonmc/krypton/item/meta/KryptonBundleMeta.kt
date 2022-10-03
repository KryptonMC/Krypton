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

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonBundleMeta(data: CompoundTag) : AbstractItemMeta<KryptonBundleMeta>(data), BundleMeta {

    override val items: PersistentList<ItemStack> = data.getList("Items", CompoundTag.ID).mapCompound(KryptonItemStack::from)

    override fun copy(data: CompoundTag): KryptonBundleMeta = KryptonBundleMeta(data)

    override fun withItems(items: List<ItemStack>): KryptonBundleMeta = KryptonBundleMeta(data.setItems(items.toImmutableList()))

    override fun addItem(item: ItemStack): KryptonBundleMeta = withItems(items.add(item))

    override fun removeItem(index: Int): KryptonBundleMeta = withItems(items.removeAt(index))

    override fun removeItem(item: ItemStack): KryptonBundleMeta = withItems(items.remove(item))

    override fun toBuilder(): BundleMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonBundleMeta(${partialToString()}, items=$items)"

    class Builder() : KryptonItemMetaBuilder<BundleMeta.Builder, BundleMeta>(), BundleMeta.Builder {

        private val items = persistentListOf<ItemStack>().builder()

        constructor(meta: BundleMeta) : this() {
            copyFrom(meta)
            items.addAll(meta.items)
        }

        override fun items(items: Iterable<ItemStack>): BundleMeta.Builder = apply {
            this.items.clear()
            this.items.addAll(items)
        }

        override fun addItem(item: ItemStack): BundleMeta.Builder = apply { items.add(item) }

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
