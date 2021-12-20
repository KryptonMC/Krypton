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
package org.kryptonmc.krypton.item.meta

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.adventure.toLegacySectionText
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.WrittenBookGeneration
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.util.convertToList
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

class KryptonWrittenBookMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val title: Component,
    override val author: Component,
    override val pages: List<Component>,
    override val generation: WrittenBookGeneration
) : AbstractItemMeta<KryptonWrittenBookMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), WrittenBookMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getName(),
        tag.getLore(),
        tag.getInt("HideFlags"),
        tag.getBlocks("CanDestroy"),
        tag.getBlocks("CanPlaceOn"),
        LegacyComponentSerializer.legacySection().deserialize(tag.getString("title")),
        LegacyComponentSerializer.legacySection().deserialize(tag.getString("author")),
        tag.getList("pages", StringTag.ID).map { LegacyComponentSerializer.legacySection().deserialize((it as StringTag).value) },
        WrittenBookGeneration.fromId(tag.getInt("generation")) ?: WrittenBookGeneration.ORIGINAL
    )

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonWrittenBookMeta = copy(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)

    override fun saveData(): CompoundTag.Builder = super.saveData().apply {
        string("title", title.toLegacySectionText())
        string("author", author.toLegacySectionText())
        list("pages", StringTag.ID, pages.map { StringTag.of(it.toJsonString()) })
        int("generation", generation.ordinal)
    }

    override fun withTitle(title: Component): KryptonWrittenBookMeta = copy(title = title)

    override fun withAuthor(author: Component): KryptonWrittenBookMeta = copy(author = author)

    override fun withPages(pages: Iterable<Component>): KryptonWrittenBookMeta = copy(pages = pages.convertToList())

    override fun withGeneration(generation: WrittenBookGeneration): KryptonWrittenBookMeta = copy(generation = generation)

    override fun addPage(page: Component): KryptonWrittenBookMeta = withPages(pages.plus(page))

    override fun removePage(index: Int): KryptonWrittenBookMeta = removePage(pages[index])

    override fun removePage(page: Component): KryptonWrittenBookMeta = withPages(pages.minus(page))

    override fun toBuilder(): WrittenBookMeta.Builder = Builder(this)

    private fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: List<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: Set<Block> = this.canDestroy,
        canPlaceOn: Set<Block> = this.canPlaceOn,
        title: Component = this.title,
        author: Component = this.author,
        pages: List<Component> = this.pages,
        generation: WrittenBookGeneration = this.generation
    ): KryptonWrittenBookMeta = KryptonWrittenBookMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        title,
        author,
        pages,
        generation
    )

    class Builder() : KryptonItemMetaBuilder<WrittenBookMeta.Builder, WrittenBookMeta>(), WrittenBookMeta.Builder {

        private var title: Component = Component.empty()
        private var author: Component = Component.empty()
        private val pages = mutableListOf<Component>()
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

        override fun build(): KryptonWrittenBookMeta = KryptonWrittenBookMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            title,
            author,
            pages,
            generation
        )
    }
}
