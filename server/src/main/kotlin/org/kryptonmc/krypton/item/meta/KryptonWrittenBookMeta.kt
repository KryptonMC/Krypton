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
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.item.data.WrittenBookGeneration
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.util.collection.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.list

class KryptonWrittenBookMeta(data: CompoundTag) : AbstractItemMeta<KryptonWrittenBookMeta>(data), WrittenBookMeta {

    override val title: Component = LegacyComponentSerializer.legacySection().deserialize(data.getString(TITLE_TAG))
    override val author: Component = LegacyComponentSerializer.legacySection().deserialize(data.getString(AUTHOR_TAG))
    override val pages: ImmutableList<Component> =
        mapToList(data, PAGES_TAG, StringTag.ID) { GsonComponentSerializer.gson().deserialize((it as StringTag).value()) }
    override val generation: WrittenBookGeneration = GENERATIONS.getOrNull(data.getInt(GENERATION_TAG)) ?: WrittenBookGeneration.ORIGINAL

    override fun copy(data: CompoundTag): KryptonWrittenBookMeta = KryptonWrittenBookMeta(data)

    override fun withTitle(title: Component): KryptonWrittenBookMeta =
        copy(data.putString(TITLE_TAG, LegacyComponentSerializer.legacySection().serialize(title)))

    override fun withAuthor(author: Component): KryptonWrittenBookMeta =
        copy(data.putString(AUTHOR_TAG, LegacyComponentSerializer.legacySection().serialize(author)))

    override fun withPages(pages: Collection<Component>): KryptonWrittenBookMeta =
        copy(put(data, PAGES_TAG, pages) { StringTag.of(LegacyComponentSerializer.legacySection().serialize(it)) })

    override fun withGeneration(generation: WrittenBookGeneration): KryptonWrittenBookMeta = copy(data.putInt(GENERATION_TAG, generation.ordinal))

    override fun withPage(page: Component): KryptonWrittenBookMeta =
        copy(data.update(PAGES_TAG, StringTag.ID) { it.add(StringTag.of(toJson(page))) })

    override fun withoutPage(index: Int): KryptonWrittenBookMeta = copy(data.update(PAGES_TAG, StringTag.ID) { it.remove(index) })

    override fun withoutPage(page: Component): KryptonWrittenBookMeta =
        copy(data.update(PAGES_TAG, StringTag.ID) { it.remove(StringTag.of(toJson(page))) })

    override fun toBuilder(): WrittenBookMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<WrittenBookMeta.Builder, WrittenBookMeta>, WrittenBookMeta.Builder {

        private var title: Component = Component.empty()
        private var author: Component = Component.empty()
        private var pages: MutableCollection<Component>
        private var generation: WrittenBookGeneration = WrittenBookGeneration.ORIGINAL

        constructor() : super() {
            pages = BuilderCollection()
        }

        constructor(meta: KryptonWrittenBookMeta) : super(meta) {
            title = meta.title
            author = meta.author
            pages = BuilderCollection(meta.pages)
            generation = meta.generation
        }

        override fun title(title: Component): WrittenBookMeta.Builder = apply { this.title = title }

        override fun author(author: Component): WrittenBookMeta.Builder = apply { this.author = author }

        override fun pages(pages: Collection<Component>): WrittenBookMeta.Builder = apply { this.pages = BuilderCollection(pages) }

        override fun addPage(page: Component): WrittenBookMeta.Builder = apply { pages.add(page) }

        override fun generation(generation: WrittenBookGeneration): WrittenBookMeta.Builder = apply { this.generation = generation }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            putString(TITLE_TAG, LegacyComponentSerializer.legacySection().serialize(title))
            putString(AUTHOR_TAG, LegacyComponentSerializer.legacySection().serialize(author))
            list(PAGES_TAG) { pages.forEach { addString(toJson(it)) } }
            putInt(GENERATION_TAG, generation.ordinal)
        }

        override fun build(): KryptonWrittenBookMeta = KryptonWrittenBookMeta(buildData().build())
    }

    companion object {

        private const val TITLE_TAG = "title"
        private const val AUTHOR_TAG = "author"
        private const val PAGES_TAG = "pages"
        private const val GENERATION_TAG = "generation"
        private val GENERATIONS = WrittenBookGeneration.values()
    }
}
