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
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.krypton.item.mask
import org.kryptonmc.krypton.util.convertToList
import org.kryptonmc.krypton.util.convertToSet
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemMeta<I : ItemMeta>(
    override val damage: Int,
    override val isUnbreakable: Boolean,
    override val customModelData: Int,
    override val name: Component?,
    override val lore: List<Component>,
    override val hideFlags: Int,
    override val canDestroy: Set<Block>,
    override val canPlaceOn: Set<Block>
) : ItemMeta {

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

    companion object {

        @JvmStatic
        protected fun <T : Tag, R> CompoundTag.getDisplay(key: String, type: Int, default: R?, mapper: (T) -> R): R? {
            if (!contains("display", CompoundTag.ID) || !getCompound("display").contains(key, type)) return default
            val tag = getCompound("display")[key] as? T ?: return default
            return mapper(tag)
        }
    }
}
