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
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.mask
import org.kryptonmc.krypton.util.convertToList
import org.kryptonmc.krypton.util.convertToSet
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.buildCompound

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemMeta<I : ItemMeta>(
    final override val damage: Int,
    final override val isUnbreakable: Boolean,
    final override val customModelData: Int,
    final override val name: Component?,
    final override val lore: List<Component>,
    final override val hideFlags: Int,
    final override val canDestroy: Set<Block>,
    final override val canPlaceOn: Set<Block>
) : ItemMeta {

    private var cachedNBT: CompoundTag? = null

    abstract fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: List<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: Set<Block> = this.canDestroy,
        canPlaceOn: Set<Block> = this.canPlaceOn
    ): I

    fun save(): CompoundTag {
        if (cachedNBT == null) cachedNBT = saveData().build()
        return cachedNBT!!
    }

    protected open fun saveData(): CompoundTag.Builder = buildCompound {
        int("Damage", damage)
        boolean("Unbreakable", isUnbreakable)
        int("CustomModelData", customModelData)
        put("display", saveDisplay().build())
        int("HideFlags", hideFlags)
        list("CanDestroy", StringTag.ID, canDestroy.map { StringTag.of(it.key().asString()) })
        list("CanPlaceOn", StringTag.ID, canPlaceOn.map { StringTag.of(it.key().asString()) })
    }

    protected open fun saveDisplay(): CompoundTag.Builder = buildCompound {
        if (name != null) string("Name", name.toJsonString())
        if (lore.isNotEmpty()) list("Lore", StringTag.ID, lore.map { StringTag.of(it.toJsonString()) })
    }

    override fun hasFlag(flag: ItemFlag): Boolean = hideFlags and flag.mask() != 0

    override fun withDamage(damage: Int): I {
        if (damage == this.damage) return this as I
        return copy(damage = damage)
    }

    override fun withUnbreakable(unbreakable: Boolean): I {
        if (unbreakable == isUnbreakable) return this as I
        return copy(isUnbreakable = unbreakable)
    }

    override fun withCustomModelData(data: Int): I {
        if (data == customModelData) return this as I
        return copy(customModelData = data)
    }

    override fun withName(name: Component?): I {
        if (name == this.name) return this as I
        return copy(name = name)
    }

    override fun withLore(lore: Iterable<Component>): I = copy(lore = lore.convertToList())

    override fun addLore(lore: Component): I = copy(lore = this.lore.plus(lore))

    override fun removeLore(index: Int): I = removeLore(lore[index])

    override fun removeLore(lore: Component): I = copy(lore = this.lore.minus(lore))

    override fun withHideFlags(flags: Int): I {
        if (flags == hideFlags) return this as I
        return copy(hideFlags = flags)
    }

    override fun withHideFlag(flag: ItemFlag): I {
        if (hideFlags and flag.mask() != 0) return this as I
        return withHideFlags(hideFlags or flag.mask())
    }

    override fun withoutHideFlag(flag: ItemFlag): I {
        if (hideFlags and flag.mask() == 0) return this as I
        return copy(hideFlags = hideFlags and flag.mask().inv())
    }

    override fun withCanDestroy(blocks: Iterable<Block>): I {
        if (canDestroy == blocks) return this as I
        return copy(canDestroy = blocks.convertToSet())
    }

    override fun withCanPlaceOn(blocks: Iterable<Block>): I {
        if (canPlaceOn == blocks) return this as I
        return copy(canPlaceOn = blocks.convertToSet())
    }

    protected open fun equalTo(other: I): Boolean = damage == other.damage &&
            isUnbreakable == other.isUnbreakable &&
            customModelData == other.customModelData &&
            name == other.name &&
            lore == other.lore &&
            hideFlags == other.hideFlags &&
            canDestroy == other.canDestroy &&
            canPlaceOn == other.canPlaceOn

    protected fun partialToString(): String = "damage=$damage, isUnbreakable=$isUnbreakable, customModelData=$customModelData, name=$name, " +
            "lore=$lore, hideFlags=$hideFlags, canDestroy=$canDestroy, canPlaceOn=$canPlaceOn"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return equalTo(other as I)
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + damage.hashCode()
        result = 31 * result + isUnbreakable.hashCode()
        result = 31 * result + customModelData.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + lore.hashCode()
        result = 31 * result + hideFlags.hashCode()
        result = 31 * result + canDestroy.hashCode()
        result = 31 * result + canPlaceOn.hashCode()
        return result
    }

    companion object {

        @JvmStatic
        protected fun <T : Tag, R> CompoundTag.getDisplay(key: String, type: Int, default: R?, mapper: (T) -> R): R? {
            if (!contains("display", CompoundTag.ID) || !getCompound("display").contains(key, type)) return default
            val tag = getCompound("display")[key] as? T ?: return default
            return mapper(tag)
        }

        @JvmStatic
        protected fun CompoundTag.getName(): Component? = getDisplay<StringTag, Component>("Name", StringTag.ID, null) {
            GsonComponentSerializer.gson().deserialize(it.value)
        }

        @JvmStatic
        protected fun CompoundTag.getLore(): List<Component> = getDisplay<ListTag, List<Component>>("Lore", ListTag.ID, emptyList()) { list ->
            list.map { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
        }!!

        @JvmStatic
        protected fun CompoundTag.getBlocks(key: String): Set<Block> = getList(key, StringTag.ID).mapTo(mutableSetOf()) {
            Registries.BLOCK[Key.key((it as StringTag).value)]!!
        }
    }
}
