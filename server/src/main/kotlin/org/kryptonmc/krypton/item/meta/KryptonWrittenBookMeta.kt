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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.adventure.toLegacySectionText
import org.kryptonmc.api.item.data.WrittenBookGeneration
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.list

class KryptonWrittenBookMeta(data: CompoundTag) : AbstractItemMeta<KryptonWrittenBookMeta>(data), WrittenBookMeta {

    override val title: Component = LegacyComponentSerializer.legacySection().deserialize(data.getString("title"))
    override val author: Component = LegacyComponentSerializer.legacySection().deserialize(data.getString("author"))
    override val pages: PersistentList<Component> = data.getList("pages", StringTag.ID)
        .mapPersistentList { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
    override val generation: WrittenBookGeneration = GENERATIONS.getOrNull(data.getInt("generation")) ?: WrittenBookGeneration.ORIGINAL

    override fun copy(data: CompoundTag): KryptonWrittenBookMeta = KryptonWrittenBookMeta(data)

    override fun withTitle(title: Component): KryptonWrittenBookMeta = copy(data.putString("title", title.toLegacySectionText()))

    override fun withAuthor(author: Component): KryptonWrittenBookMeta = copy(data.putString("author", author.toLegacySectionText()))

    override fun withPages(pages: Iterable<Component>): KryptonWrittenBookMeta = copy(data.putPages(pages.toImmutableList()))

    override fun withGeneration(generation: WrittenBookGeneration): KryptonWrittenBookMeta = copy(data.putInt("generation", generation.ordinal))

    override fun addPage(page: Component): KryptonWrittenBookMeta = withPages(pages.add(page))

    override fun removePage(index: Int): KryptonWrittenBookMeta = withPages(pages.removeAt(index))

    override fun removePage(page: Component): KryptonWrittenBookMeta = withPages(pages.remove(page))

    override fun toBuilder(): WrittenBookMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<WrittenBookMeta.Builder, WrittenBookMeta>(), WrittenBookMeta.Builder {

        private var title: Component = Component.empty()
        private var author: Component = Component.empty()
        private val pages = persistentListOf<Component>().builder()
        private var generation = WrittenBookGeneration.ORIGINAL

        constructor(meta: WrittenBookMeta) : this() {
            copyFrom(meta)
            title = meta.title
            author = meta.author
            pages.addAll(meta.pages)
            generation = meta.generation
        }

        override fun title(title: Component): WrittenBookMeta.Builder = apply { this.title = title }

        override fun author(author: Component): WrittenBookMeta.Builder = apply { this.author = author }

        override fun pages(pages: Iterable<Component>): WrittenBookMeta.Builder = apply {
            this.pages.clear()
            this.pages.addAll(pages)
        }

        override fun addPage(page: Component): WrittenBookMeta.Builder = apply { pages.add(page) }

        override fun generation(generation: WrittenBookGeneration): WrittenBookMeta.Builder = apply { this.generation = generation }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            string("title", title.toLegacySectionText())
            string("author", author.toLegacySectionText())
            list("pages") { pages.forEach { addString(it.toJson()) } }
            int("generation", generation.ordinal)
        }

        override fun build(): KryptonWrittenBookMeta = KryptonWrittenBookMeta(buildData().build())
    }

    companion object {

        private val GENERATIONS = WrittenBookGeneration.values()
    }
}

private fun CompoundTag.putPages(pages: List<Component>): CompoundTag {
    if (pages.isEmpty()) return remove("pages")
    return put("pages", list { pages.forEach { addString(it.toJson()) } })
}
