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

import com.google.common.collect.ImmutableCollection
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemAttributeModifier
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.krypton.item.KryptonItemAttributeModifier
import org.kryptonmc.krypton.item.mask
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.list

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemMeta<I : ItemMeta>(val data: CompoundTag) : ItemMeta {

    final override val damage: Int = data.getInt(DAMAGE_TAG)
    final override val isUnbreakable: Boolean = data.getBoolean(UNBREAKABLE_TAG)
    final override val customModelData: Int = data.getInt(CUSTOM_MODEL_DATA_TAG)
    final override val name: Component? =
        getDisplay<StringTag, _>(data, NAME_TAG, StringTag.ID, null) { GsonComponentSerializer.gson().deserialize(it.value()) }
    final override val lore: ImmutableList<Component> = getLore(data)
    final override val hideFlags: Int = data.getInt(HIDE_FLAGS_TAG)
    final override val canDestroy: ImmutableSet<Block> = getBlocks(data, CAN_DESTROY_TAG)
    final override val canPlaceOn: ImmutableSet<Block> = getBlocks(data, CAN_PLACE_ON_TAG)
    final override val attributeModifiers: ImmutableList<ItemAttributeModifier> =
        mapToList(data, MODIFIERS_TAG, CompoundTag.ID) { KryptonItemAttributeModifier.load(it as CompoundTag) }

    abstract fun copy(data: CompoundTag): I

    final override fun hasFlag(flag: ItemFlag): Boolean = hideFlags and flag.mask() != 0

    final override fun withDamage(damage: Int): I {
        if (damage == this.damage) return this as I
        return copy(data.putInt(DAMAGE_TAG, damage))
    }

    final override fun withUnbreakable(unbreakable: Boolean): I {
        if (unbreakable == isUnbreakable) return this as I
        return copy(data.putBoolean(UNBREAKABLE_TAG, unbreakable))
    }

    final override fun withCustomModelData(data: Int): I {
        if (data == customModelData) return this as I
        return copy(this.data.putInt(CUSTOM_MODEL_DATA_TAG, data))
    }

    final override fun withName(name: Component?): I {
        if (name == this.name) return this as I
        if (name == null) return copy(data.update(DISPLAY_TAG) { it.remove(NAME_TAG) })
        return copy(data.update(DISPLAY_TAG) { it.putString(NAME_TAG, toJson(name)) })
    }

    final override fun withLore(lore: List<Component>): I = copy(put(data, LORE_TAG, lore) { StringTag.of(toJson(it)) })

    final override fun withLore(lore: Component): I = copy(data.update(LORE_TAG, StringTag.ID) { it.add(StringTag.of(toJson(lore))) })

    final override fun withoutLore(index: Int): I = copy(data.update(LORE_TAG, StringTag.ID) { it.remove(index) })

    final override fun withoutLore(lore: Component): I = copy(data.update(LORE_TAG, StringTag.ID) { it.remove(StringTag.of(toJson(lore))) })

    final override fun withHideFlags(flags: Int): I {
        if (flags == hideFlags) return this as I
        return copy(data.putInt(HIDE_FLAGS_TAG, flags))
    }

    final override fun withHideFlag(flag: ItemFlag): I {
        if (hideFlags and flag.mask() != 0) return this as I
        return withHideFlags(hideFlags or flag.mask())
    }

    final override fun withoutHideFlag(flag: ItemFlag): I {
        if (hideFlags and flag.mask() == 0) return this as I
        return withHideFlags(hideFlags and flag.mask().inv())
    }

    final override fun withCanDestroy(blocks: Collection<Block>): I = copy(putBlocks(data, CAN_DESTROY_TAG, blocks))

    final override fun withCanPlaceOn(blocks: Collection<Block>): I = copy(putBlocks(data, CAN_PLACE_ON_TAG, blocks))

    final override fun withAttributeModifiers(modifiers: Collection<ItemAttributeModifier>): I =
        copy(put(data, MODIFIERS_TAG, modifiers, KryptonItemAttributeModifier::save))

    final override fun withoutAttributeModifiers(): I = copy(data.remove(MODIFIERS_TAG))

    final override fun withAttributeModifier(modifier: ItemAttributeModifier): I =
        copy(data.update(MODIFIERS_TAG, CompoundTag.ID) { it.add(KryptonItemAttributeModifier.save(modifier)) })

    final override fun withoutAttributeModifier(modifier: ItemAttributeModifier): I =
        copy(data.update(MODIFIERS_TAG, CompoundTag.ID) { it.remove(KryptonItemAttributeModifier.save(modifier)) })

    protected fun partialToString(): String = "damage=$damage, isUnbreakable=$isUnbreakable, customModelData=$customModelData, name=$name, " +
            "lore=$lore, hideFlags=$hideFlags, canDestroy=$canDestroy, canPlaceOn=$canPlaceOn"

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data == (other as AbstractItemMeta<*>).data
    }

    final override fun hashCode(): Int = data.hashCode()

    companion object {

        private const val DISPLAY_TAG = "display"
        private const val DAMAGE_TAG = "Damage"
        private const val UNBREAKABLE_TAG = "Unbreakable"
        private const val CUSTOM_MODEL_DATA_TAG = "CustomModelData"
        private const val NAME_TAG = "Name"
        private const val LORE_TAG = "Lore"
        private const val HIDE_FLAGS_TAG = "HideFlags"
        private const val CAN_DESTROY_TAG = "CanDestroy"
        private const val CAN_PLACE_ON_TAG = "CanPlaceOn"
        private const val MODIFIERS_TAG = "AttributeModifiers"

        @JvmStatic
        protected fun <T : Tag, R> getDisplay(data: CompoundTag, key: String, type: Int, default: R?, mapper: (T) -> R): R? {
            if (!data.contains(DISPLAY_TAG, CompoundTag.ID)) return default
            val displayTag = data.getCompound(DISPLAY_TAG)
            if (!displayTag.contains(key, type)) return default
            val display = displayTag.get(key) as? T ?: return default
            return mapper(display)
        }

        @JvmStatic
        private fun getLore(data: CompoundTag): ImmutableList<Component> =
            getDisplay<ListTag, _>(data, LORE_TAG, ListTag.ID, ImmutableList.of()) { list ->
                val builder = ImmutableList.builder<Component>()
                list.forEach { builder.add(GsonComponentSerializer.gson().deserialize((it as StringTag).value())) }
                builder.build()
            }!!

        @JvmStatic
        private fun getBlocks(data: CompoundTag, key: String): ImmutableSet<Block> =
            mapToSet(data, key, StringTag.ID) { KryptonRegistries.BLOCK.get(Key.key((it as StringTag).value())) }

        @JvmStatic
        private fun putBlocks(data: CompoundTag, key: String, blocks: Collection<Block>): CompoundTag =
            put(data, key, blocks) { StringTag.of(it.key().asString()) }

        @JvmStatic
        protected inline fun <T> put(data: CompoundTag, key: String, values: Collection<T>, addAction: (T) -> Tag): CompoundTag {
            if (values.isEmpty()) return data.remove(key)
            return data.put(key, list { values.forEach { add(addAction(it)) } })
        }

        @JvmStatic
        protected fun toJson(input: Component): String = GsonComponentSerializer.gson().serialize(input)

        @JvmStatic
        protected inline fun <R> mapToList(data: CompoundTag, key: String, type: Int, mapper: (Tag) -> R?): ImmutableList<R> =
            mapTo(data, key, type, mapper) { ImmutableList.builder() }

        @JvmStatic
        protected inline fun <R> mapToSet(data: CompoundTag, key: String, type: Int, mapper: (Tag) -> R?): ImmutableSet<R> =
            mapTo(data, key, type, mapper) { ImmutableSet.builder() }

        @JvmStatic
        protected inline fun <C : IC<R>, B : ICB<R>, R> mapTo(data: CompoundTag, key: String, type: Int, mapper: (Tag) -> R?, builder: () -> B): C {
            val result = builder()
            data.getList(key, type).forEach { mapper(it)?.let(result::add) }
            return result.build() as C
        }
    }
}

private typealias IC<E> = ImmutableCollection<E>
private typealias ICB<E> = ImmutableCollection.Builder<E>
