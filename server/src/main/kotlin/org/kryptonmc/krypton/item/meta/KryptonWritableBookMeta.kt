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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.item.meta.WritableBookMeta
import org.kryptonmc.krypton.adventure.toJson
import org.kryptonmc.krypton.adventure.toLegacySectionText
import org.kryptonmc.krypton.util.BuilderCollection
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.list

class KryptonWritableBookMeta(data: CompoundTag) : AbstractItemMeta<KryptonWritableBookMeta>(data), WritableBookMeta {

    override val pages: ImmutableList<Component> =
        data.mapToList("pages", StringTag.ID) { LegacyComponentSerializer.legacySection().deserialize((it as StringTag).value()) }

    override fun copy(data: CompoundTag): KryptonWritableBookMeta = KryptonWritableBookMeta(data)

    override fun withPages(pages: Collection<Component>): KryptonWritableBookMeta = copy(data.putPages(pages))

    override fun withPage(page: Component): KryptonWritableBookMeta = copy(data.update("pages", StringTag.ID) { it.add(page.toLegacy()) })

    override fun withoutPage(index: Int): KryptonWritableBookMeta = copy(data.update("pages", StringTag.ID) { it.remove(index) })

    override fun withoutPage(page: Component): KryptonWritableBookMeta = copy(data.update("pages", StringTag.ID) { it.remove(page.toLegacy()) })

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
            if (pages.isNotEmpty()) list("pages") { pages.forEach { addString(it.toJson()) } }
        }

        override fun build(): KryptonWritableBookMeta = KryptonWritableBookMeta(buildData().build())
    }
}

private fun Component.toLegacy(): StringTag = StringTag.of(toLegacySectionText())

private fun CompoundTag.putPages(pages: Collection<Component>): CompoundTag =
    if (pages.isEmpty()) remove("pages") else put("pages", list { pages.forEach { addString(it.toJson()) } })
