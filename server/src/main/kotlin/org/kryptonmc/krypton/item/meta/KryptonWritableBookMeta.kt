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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.item.meta.WritableBookMeta
import org.kryptonmc.krypton.util.collection.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.list

class KryptonWritableBookMeta(data: CompoundTag) : AbstractItemMeta<KryptonWritableBookMeta>(data), WritableBookMeta {

    override val pages: ImmutableList<Component> =
        mapToList(data, PAGES_TAG, StringTag.ID) { LegacyComponentSerializer.legacySection().deserialize((it as StringTag).value()) }

    override fun copy(data: CompoundTag): KryptonWritableBookMeta = KryptonWritableBookMeta(data)

    override fun withPages(pages: Collection<Component>): KryptonWritableBookMeta = copy(put(data, PAGES_TAG, pages, ::toLegacy))

    override fun withPage(page: Component): KryptonWritableBookMeta = copy(data.update(PAGES_TAG, StringTag.ID) { it.add(toLegacy(page)) })

    override fun withoutPage(index: Int): KryptonWritableBookMeta = copy(data.update(PAGES_TAG, StringTag.ID) { it.remove(index) })

    override fun withoutPage(page: Component): KryptonWritableBookMeta = copy(data.update(PAGES_TAG, StringTag.ID) { it.remove(toLegacy(page)) })

    override fun toBuilder(): WritableBookMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<WritableBookMeta.Builder, WritableBookMeta>, WritableBookMeta.Builder {

        private var pages: MutableCollection<Component>

        constructor() {
            pages = BuilderCollection()
        }

        constructor(meta: KryptonWritableBookMeta) {
            pages = BuilderCollection(meta.pages)
        }

        override fun pages(pages: Collection<Component>): Builder = apply { this.pages = BuilderCollection() }

        override fun addPage(page: Component): Builder = apply { pages.add(page) }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (pages.isNotEmpty()) list(PAGES_TAG) { pages.forEach { addString(toJson(it)) } }
        }

        override fun build(): KryptonWritableBookMeta = KryptonWritableBookMeta(buildData().build())
    }

    companion object {

        private const val PAGES_TAG = "pages"

        @JvmStatic
        private fun toLegacy(input: Component): StringTag = StringTag.of(LegacyComponentSerializer.legacySection().serialize(input))
    }
}
