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
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.nbt.CompoundTag

@Suppress("EqualsOrHashCode")
class KryptonBundleMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: PersistentList<Component>,
    hideFlags: Int,
    canDestroy: ImmutableSet<Block>,
    canPlaceOn: ImmutableSet<Block>,
    override val items: PersistentList<ItemStack>
) : AbstractItemMeta<KryptonBundleMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), BundleMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getName(),
        tag.getLore(),
        tag.getInt("HideFlags"),
        tag.getBlocks("CanDestroy"),
        tag.getBlocks("CanPlaceOn"),
        tag.getList("Items", CompoundTag.ID).mapPersistentList { KryptonItemStack(it as CompoundTag) }
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
    ): KryptonBundleMeta = KryptonBundleMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, items)

    override fun saveData(): CompoundTag.Builder = super.saveData().apply {
        list("Items", CompoundTag.ID, items.map { (it as KryptonItemStack).save() })
    }

    override fun withItems(items: List<ItemStack>): KryptonBundleMeta = KryptonBundleMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        items.toPersistentList()
    )

    override fun addItem(item: ItemStack): KryptonBundleMeta = withItems(items.add(item))

    override fun removeItem(index: Int): KryptonBundleMeta = withItems(items.removeAt(index))

    override fun removeItem(item: ItemStack): KryptonBundleMeta = withItems(items.remove(item))

    override fun toBuilder(): BundleMeta.Builder = Builder(this)

    override fun equalTo(other: KryptonBundleMeta): Boolean = super.equalTo(other) && items == other.items

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }

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

        override fun build(): BundleMeta = KryptonBundleMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore.build(),
            hideFlags,
            canDestroy.build(),
            canPlaceOn.build(),
            items.build()
        )
    }
}
