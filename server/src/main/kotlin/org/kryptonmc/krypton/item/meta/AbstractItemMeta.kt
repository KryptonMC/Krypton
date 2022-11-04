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
import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemAttributeModifier
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.adventure.toJson
import org.kryptonmc.krypton.item.KryptonItemAttributeModifier
import org.kryptonmc.krypton.item.mask
import org.kryptonmc.krypton.item.save
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.list

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemMeta<I : ItemMeta>(val data: CompoundTag) : ItemMeta {

    final override val damage: Int = data.getInt("Damage")
    final override val isUnbreakable: Boolean = data.getBoolean("Unbreakable")
    final override val customModelData: Int = data.getInt("CustomModelData")
    final override val name: Component? = data.getDisplay("Name", StringTag.ID, null, StringTag::toComponent)
    final override val lore: ImmutableList<Component> = data.getLore()
    final override val hideFlags: Int = data.getInt("HideFlags")
    final override val canDestroy: ImmutableSet<Block> = data.getBlocks("CanDestroy")
    final override val canPlaceOn: ImmutableSet<Block> = data.getBlocks("CanPlaceOn")
    final override val attributeModifiers: ImmutableList<ItemAttributeModifier> =
        data.mapToList("AttributeModifiers", CompoundTag.ID) { KryptonItemAttributeModifier.from(it as CompoundTag) }

    abstract fun copy(data: CompoundTag): I

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
        if (name == null) return copy(data.update("display") { it.remove("Name") })
        return copy(data.update("display") { it.putString("Name", name.toJson()) })
    }

    final override fun withLore(lore: List<Component>): I = copy(data.set("Lore", lore) { StringTag.of(it.toJson()) })

    final override fun withLore(lore: Component): I = copy(data.update("Lore", StringTag.ID) { it.add(StringTag.of(lore.toJson())) })

    final override fun withoutLore(index: Int): I = copy(data.update("Lore", StringTag.ID) { it.remove(index) })

    final override fun withoutLore(lore: Component): I = copy(data.update("Lore", StringTag.ID) { it.remove(StringTag.of(lore.toJson())) })

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

    final override fun withCanDestroy(blocks: Collection<Block>): I = copy(data.setBlocks("CanDestroy", blocks))

    final override fun withCanPlaceOn(blocks: Collection<Block>): I = copy(data.setBlocks("CanPlaceOn", blocks))

    final override fun withAttributeModifiers(modifiers: Collection<ItemAttributeModifier>): I =
        copy(data.set("AttributeModifiers", modifiers) { it.save() })

    final override fun withoutAttributeModifiers(): I = copy(data.remove("AttributeModifiers"))

    final override fun withAttributeModifier(modifier: ItemAttributeModifier): I =
        copy(data.update("AttributeModifiers", CompoundTag.ID) { it.add(modifier.save()) })

    final override fun withoutAttributeModifier(modifier: ItemAttributeModifier): I =
        copy(data.update("AttributeModifiers", CompoundTag.ID) { it.remove(modifier.save()) })

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
    if (!contains("display", CompoundTag.ID)) return default
    val displayTag = getCompound("display")
    if (!displayTag.contains(key, type)) return default
    val display = displayTag.get(key) as? T ?: return default
    return mapper(display)
}

private fun StringTag.toComponent(): Component = GsonComponentSerializer.gson().deserialize(value())

private fun CompoundTag.getLore(): ImmutableList<Component> = getDisplay<ListTag, _>("Lore", ListTag.ID, ImmutableList.of()) { list ->
    val builder = ImmutableList.builder<Component>()
    list.forEach { builder.add((it as StringTag).toComponent()) }
    builder.build()
}!!

private fun CompoundTag.getBlocks(key: String): ImmutableSet<Block> =
    mapToSet(key, StringTag.ID) { Registries.BLOCK.get(Key.key((it as StringTag).value())) }

private fun CompoundTag.setBlocks(key: String, blocks: Collection<Block>): CompoundTag = set(key, blocks) { StringTag.of(it.key().asString()) }

private inline fun <T> CompoundTag.set(key: String, values: Collection<T>, addAction: (T) -> Tag): CompoundTag {
    if (values.isEmpty()) return remove(key)
    return put(key, list { values.forEach { add(addAction(it)) } })
}
