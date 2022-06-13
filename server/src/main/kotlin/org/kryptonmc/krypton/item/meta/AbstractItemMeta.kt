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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.mask
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.krypton.util.mapPersistentSet
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.list

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemMeta<I : ItemMeta>(protected val data: CompoundTag) : ItemMeta {

    final override val damage: Int = data.getInt("Damage")
    final override val isUnbreakable: Boolean = data.getBoolean("Unbreakable")
    final override val customModelData: Int = data.getInt("CustomModelData")
    final override val name: Component? = data.getName()
    final override val lore: PersistentList<Component> = data.getLore()
    final override val hideFlags: Int = data.getInt("HideFlags")
    final override val canDestroy: ImmutableSet<Block> = data.getBlocks("CanDestroy")
    final override val canPlaceOn: ImmutableSet<Block> = data.getBlocks("CanPlaceOn")

    abstract fun copy(data: CompoundTag): I

    fun save(): CompoundTag = data

    final override fun hasFlag(flag: ItemFlag): Boolean = hideFlags and flag.mask() != 0

    final override fun withDamage(damage: Int): I {
        if (damage == this.damage) return this as I
        return copy(data.putInt("Damage", damage))
    }

    final override fun withUnbreakable(unbreakable: Boolean): I {
        if (unbreakable == isUnbreakable) return this as I
        return copy(data.putBoolean("Unbreakable", unbreakable))
    }

    final override fun withCustomModelData(data: Int): I {
        if (data == customModelData) return this as I
        return copy(this.data.putInt("CustomModelData", data))
    }

    final override fun withName(name: Component?): I {
        if (name == this.name) return this as I
        if (name == null) return copy(data.update("display") { remove("Name") })
        return copy(data.update("display") { putString("Name", name.toJson()) })
    }

    final override fun withLore(lore: Iterable<Component>): I = copy(data.putLore(lore.toImmutableList()))

    final override fun addLore(lore: Component): I = withLore(this.lore.add(lore))

    final override fun removeLore(index: Int): I = withLore(this.lore.removeAt(index))

    final override fun removeLore(lore: Component): I = withLore(this.lore.remove(lore))

    final override fun withHideFlags(flags: Int): I {
        if (flags == hideFlags) return this as I
        return copy(data.putInt("HideFlags", flags))
    }

    final override fun withHideFlag(flag: ItemFlag): I {
        if (hideFlags and flag.mask() != 0) return this as I
        return withHideFlags(hideFlags or flag.mask())
    }

    final override fun withoutHideFlag(flag: ItemFlag): I {
        if (hideFlags and flag.mask() == 0) return this as I
        return withHideFlags(hideFlags and flag.mask().inv())
    }

    final override fun withCanDestroy(blocks: Iterable<Block>): I = copy(data.putBlocks("CanDestroy", blocks.toImmutableSet()))

    final override fun withCanPlaceOn(blocks: Iterable<Block>): I = copy(data.putBlocks("CanPlaceOn", blocks.toImmutableSet()))

    protected fun partialToString(): String = "damage=$damage, isUnbreakable=$isUnbreakable, customModelData=$customModelData, name=$name, " +
            "lore=$lore, hideFlags=$hideFlags, canDestroy=$canDestroy, canPlaceOn=$canPlaceOn"

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data == (other as AbstractItemMeta<*>).data
    }

    final override fun hashCode(): Int = data.hashCode()
}

@Suppress("UNCHECKED_CAST")
fun <T : Tag, R> CompoundTag.getDisplay(key: String, type: Int, default: R?, mapper: (T) -> R): R? {
    if (!contains("display", CompoundTag.ID) || !getCompound("display").contains(key, type)) return default
    val tag = getCompound("display")[key] as? T ?: return default
    return mapper(tag)
}

private fun CompoundTag.getName(): Component? = getDisplay<StringTag, _>("Name", StringTag.ID, null) {
    GsonComponentSerializer.gson().deserialize(it.value)
}

private fun CompoundTag.getLore(): PersistentList<Component> = getDisplay<ListTag, _>("Lore", ListTag.ID, persistentListOf()) { list ->
    list.mapPersistentList { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
}!!

private fun CompoundTag.getBlocks(key: String): ImmutableSet<Block> = getList(key, StringTag.ID).mapPersistentSet {
    Registries.BLOCK[Key.key((it as StringTag).value)]!!
}

private fun CompoundTag.putLore(lore: List<Component>): CompoundTag {
    if (lore.isEmpty()) remove("Lore")
    return put("Lore", list { lore.forEach { addString(it.toJson()) } })
}

private fun CompoundTag.putBlocks(key: String, blocks: Set<Block>): CompoundTag {
    if (blocks.isEmpty()) return remove(key)
    return put(key, list { blocks.forEach { addString(it.key().asString()) } })
}
