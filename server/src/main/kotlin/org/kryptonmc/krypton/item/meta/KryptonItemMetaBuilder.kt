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
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemAttribute
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.krypton.item.mask
import org.kryptonmc.krypton.item.save
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound

@Suppress("UNCHECKED_CAST")
abstract class KryptonItemMetaBuilder<B : ItemMetaBuilder<B, I>, I : ItemMeta> : ItemMetaBuilder<B, I> {

    private var damage = 0
    private var unbreakable = false
    private var customModelData = 0
    private var name: Component? = null
    private val lore = persistentListOf<Component>().builder()
    private var hideFlags = 0
    private val canDestroy = persistentSetOf<Block>().builder()
    private val canPlaceOn = persistentSetOf<Block>().builder()
    private val attributes = persistentSetOf<ItemAttribute>().builder()

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

    final override fun attributeModifiers(attributes: Set<ItemAttribute>): B = apply {
        this.attributes.clear()
        this.attributes.addAll(attributes)
    } as B

    protected fun copyFrom(meta: I) {
        damage = meta.damage
        unbreakable = meta.isUnbreakable
        customModelData = meta.customModelData
        name = meta.name
        lore.addAll(meta.lore)
        hideFlags = meta.hideFlags
        canDestroy.addAll(meta.canDestroy)
        canPlaceOn.addAll(meta.canPlaceOn)
        attributes.addAll(meta.attributeModifiers)
    }

    protected open fun buildData(): CompoundTag.Builder = buildCompound {
        int("Damage", damage)
        boolean("Unbreakable", unbreakable)
        int("CustomModelData", customModelData)
        compound("display") {
            if (name != null) string("Name", name!!.toJson())
            if (lore.isNotEmpty()) list("Lore", StringTag.ID, lore.map { StringTag.of(it.toJson()) })
        }
        int("HideFlags", hideFlags)
        list("CanDestroy", StringTag.ID, canDestroy.map { StringTag.of(it.key().asString()) })
        list("CanPlaceOn", StringTag.ID, canPlaceOn.map { StringTag.of(it.key().asString()) })
        if (attributes.isNotEmpty()) list("AttributeModifiers") { attributes.forEach { add(it.save()) } }
    }
}
