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

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag

@Suppress("UNCHECKED_CAST")
class KryptonBundleMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val items: List<ItemStack>
) : AbstractItemMeta<KryptonBundleMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), BundleMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getDisplay<StringTag, Component>("Name", StringTag.ID, null) { GsonComponentSerializer.gson().deserialize(it.value) },
        tag.getDisplay<ListTag, List<Component>>("Lore", ListTag.ID, emptyList()) { list ->
            list.map { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
        }!!,
        tag.getInt("HideFlags"),
        tag.getList("CanDestroy", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! },
        tag.getList("CanPlaceOn", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! },
        tag.getList("Items", CompoundTag.ID).map { KryptonItemStack(it as CompoundTag) }
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
    ): KryptonBundleMeta = KryptonBundleMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, items)

    override fun withItems(items: List<ItemStack>): KryptonBundleMeta = KryptonBundleMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        items as List<KryptonItemStack>
    )

    override fun addItem(item: ItemStack): KryptonBundleMeta = withItems(items.plus(item))

    override fun removeItem(index: Int): KryptonBundleMeta = removeItem(items[index])

    override fun removeItem(item: ItemStack): KryptonBundleMeta = withItems(items.minus(item))

    override fun toBuilder(): BundleMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<BundleMeta.Builder, BundleMeta>(), BundleMeta.Builder {

        private val items = mutableListOf<ItemStack>()

        constructor(meta: BundleMeta) : this() {
            copyFrom(meta)
            items.addAll(meta.items)
        }

        override fun items(items: Iterable<ItemStack>): BundleMeta.Builder = apply {
            this.items.clear()
            this.items.addAll(items)
        }

        override fun addItem(item: ItemStack): BundleMeta.Builder = apply { items.add(item) }

        override fun build(): BundleMeta = KryptonBundleMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            items
        )
    }
}
