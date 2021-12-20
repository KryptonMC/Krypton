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
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.WritableBookMeta
import org.kryptonmc.krypton.util.convertToList

class KryptonWritableBookMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val pages: List<Component>
) : AbstractItemMeta<KryptonWritableBookMeta>(
    damage,
    isUnbreakable,
    customModelData,
    name,
    lore,
    hideFlags,
    canDestroy,
    canPlaceOn
), WritableBookMeta {

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonWritableBookMeta = KryptonWritableBookMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, pages)

    override fun withPages(pages: Iterable<Component>): KryptonWritableBookMeta = KryptonWritableBookMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        pages.convertToList()
    )

    override fun addPage(page: Component): KryptonWritableBookMeta = withPages(pages.plus(page))

    override fun removePage(index: Int): KryptonWritableBookMeta = removePage(pages[index])

    override fun removePage(page: Component): KryptonWritableBookMeta = withPages(pages.minus(page))

    override fun toBuilder(): WritableBookMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<WritableBookMeta.Builder, WritableBookMeta>(), WritableBookMeta.Builder {

        private val pages = mutableListOf<Component>()

        constructor(meta: WritableBookMeta) : this() {
            copyFrom(meta)
            pages.addAll(meta.pages)
        }

        override fun pages(pages: Iterable<Component>): WritableBookMeta.Builder = apply {
            this.pages.clear()
            this.pages.addAll(pages)
        }

        override fun addPage(page: Component): WritableBookMeta.Builder = apply { pages.add(page) }

        override fun build(): KryptonWritableBookMeta = KryptonWritableBookMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            pages
        )
    }
}
