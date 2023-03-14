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
package org.kryptonmc.krypton.item.meta

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.util.collection.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonBundleMeta(data: CompoundTag) : AbstractItemMeta<KryptonBundleMeta>(data), BundleMeta {

    override val items: ImmutableList<ItemStack> = mapToList(data, ITEMS_TAG, CompoundTag.ID) { KryptonItemStack.from(it as CompoundTag) }

    override fun copy(data: CompoundTag): KryptonBundleMeta = KryptonBundleMeta(data)

    override fun withItems(items: List<ItemStack>): KryptonBundleMeta = copy(put(data, ITEMS_TAG, items) { it.downcast().save() })

    override fun withItem(item: ItemStack): KryptonBundleMeta = copy(data.update(ITEMS_TAG, CompoundTag.ID) { it.add(item.downcast().save()) })

    override fun withoutItem(index: Int): KryptonBundleMeta = copy(data.update(ITEMS_TAG, CompoundTag.ID) { it.remove(index) })

    override fun withoutItem(item: ItemStack): KryptonBundleMeta = copy(data.update(ITEMS_TAG, CompoundTag.ID) { it.remove(item.downcast().save()) })

    override fun toBuilder(): BundleMeta.Builder = Builder(this)

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
            if (items.isNotEmpty()) list(ITEMS_TAG) { items.forEach { it.downcast().save() } }
        }
    }

    companion object {

        private const val ITEMS_TAG = "Items"
    }
}
