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

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.krypton.item.mask

@Suppress("UNCHECKED_CAST")
abstract class KryptonItemMetaBuilder<B : ItemMetaBuilder<B, I>, I : ItemMeta> : ItemMetaBuilder<B, I> {

    protected var damage = 0
    protected var unbreakable = false
    protected var customModelData = 0
    protected var name: Component? = null
    protected val lore = persistentListOf<Component>().builder()
    protected var hideFlags = 0
    protected val canDestroy = persistentSetOf<Block>().builder()
    protected val canPlaceOn = persistentSetOf<Block>().builder()

    final override fun damage(damage: Int): B = apply { this.damage = damage } as B

    final override fun unbreakable(value: Boolean): B = apply { unbreakable = value } as B

    final override fun customModelData(data: Int): B = apply { customModelData = data } as B

    final override fun name(name: Component?): B = apply { this.name = name } as B

    final override fun lore(lore: Iterable<Component>): B = apply {
        this.lore.clear()
        this.lore.addAll(lore)
    } as B

    final override fun addLore(lore: Component): B = apply { this.lore.add(lore) } as B

    final override fun hideFlags(flags: Int): B = apply { hideFlags = flags } as B

    final override fun addFlag(flag: ItemFlag): B = apply { hideFlags = hideFlags or flag.mask() } as B

    final override fun canDestroy(blocks: Iterable<Block>): B = apply {
        canDestroy.clear()
        canDestroy.addAll(blocks)
    } as B

    final override fun addCanDestroy(block: Block): B = apply { canDestroy.add(block) } as B

    final override fun canPlaceOn(blocks: Iterable<Block>): B = apply {
        canPlaceOn.clear()
        canPlaceOn.addAll(blocks)
    } as B

    final override fun addCanPlaceOn(block: Block): B = apply { canPlaceOn.add(block) } as B

    protected fun copyFrom(meta: I) {
        damage = meta.damage
        unbreakable = meta.isUnbreakable
        customModelData = meta.customModelData
        name = meta.name
        lore.addAll(meta.lore)
        hideFlags = meta.hideFlags
        canDestroy.addAll(meta.canDestroy)
        canPlaceOn.addAll(meta.canPlaceOn)
    }
}
