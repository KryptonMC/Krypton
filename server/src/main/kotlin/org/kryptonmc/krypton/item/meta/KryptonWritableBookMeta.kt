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

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.api.item.meta.WritableBookMeta
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

class KryptonWritableBookMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: PersistentList<Component>,
    hideFlags: Int,
    canDestroy: ImmutableSet<Block>,
    canPlaceOn: ImmutableSet<Block>,
    override val pages: PersistentList<Component>
) : AbstractItemMeta<KryptonWritableBookMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn),
    WritableBookMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getName(),
        tag.getLore(),
        tag.getInt("HideFlags"),
        tag.getBlocks("CanDestroy"),
        tag.getBlocks("CanPlaceOn"),
        tag.getList("pages", StringTag.ID).mapPersistentList { LegacyComponentSerializer.legacySection().deserialize((it as StringTag).value) },
    )

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: PersistentList<Component>,
        hideFlags: Int,
        canDestroy: ImmutableSet<Block>,
        canPlaceOn: ImmutableSet<Block>
    ): KryptonWritableBookMeta = KryptonWritableBookMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, pages)

    override fun saveData(): CompoundTag.Builder = super.saveData().apply {
        list("pages", StringTag.ID, pages.map { StringTag.of(it.toJson()) })
    }

    override fun withPages(pages: Iterable<Component>): KryptonWritableBookMeta = KryptonWritableBookMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        pages.toPersistentList()
    )

    override fun addPage(page: Component): KryptonWritableBookMeta = withPages(pages.add(page))

    override fun removePage(index: Int): KryptonWritableBookMeta = withPages(pages.removeAt(index))

    override fun removePage(page: Component): KryptonWritableBookMeta = withPages(pages.remove(page))

    override fun toBuilder(): WritableBookMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<WritableBookMeta.Builder, WritableBookMeta>(), WritableBookMeta.Builder {

        private val pages = persistentListOf<Component>().builder()

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
            lore.build(),
            hideFlags,
            canDestroy.build(),
            canPlaceOn.build(),
            pages.build()
        )
    }
}
