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
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.api.item.meta.CompassMeta
import org.kryptonmc.api.item.meta.CrossbowMeta
import org.kryptonmc.api.item.meta.FireworkRocketMeta
import org.kryptonmc.api.item.meta.FireworkStarMeta
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.api.item.meta.LeatherArmorMeta
import org.kryptonmc.api.item.meta.PlayerHeadMeta
import org.kryptonmc.api.item.meta.WritableBookMeta
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag

class KryptonItemMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>
) : AbstractItemMeta<KryptonItemMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), ItemMeta {

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
        tag.getList("CanPlaceOn", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! }
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
    ): KryptonItemMeta = KryptonItemMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)

    class Builder : KryptonItemMetaBuilder<ItemMeta.Builder, ItemMeta>(), ItemMeta.Builder {

        override fun build(): KryptonItemMeta = KryptonItemMeta(damage, unbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)
    }

    object Factory : ItemMeta.Factory {

        override fun builder(): ItemMeta.Builder = Builder()

        override fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = KryptonItemMeta.builder(type)
    }

    companion object {

        private val BUILDERS_BY_TYPE: Map<Class<out ItemMetaBuilder.Provider<*>>, () -> ItemMetaBuilder<*, *>> = mapOf(
            BundleMeta::class.java to { KryptonBundleMeta.Builder() },
            CompassMeta::class.java to { KryptonCompassMeta.Builder() },
            CrossbowMeta::class.java to { KryptonCrossbowMeta.Builder() },
            FireworkRocketMeta::class.java to { KryptonFireworkRocketMeta.Builder() },
            FireworkStarMeta::class.java to { KryptonFireworkStarMeta.Builder() },
            LeatherArmorMeta::class.java to { KryptonLeatherArmorMeta.Builder() },
            PlayerHeadMeta::class.java to { KryptonPlayerHeadMeta.Builder() },
            WritableBookMeta::class.java to { KryptonWritableBookMeta.Builder() },
            WrittenBookMeta::class.java to { KryptonWrittenBookMeta.Builder() }
        )

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <B : ItemMetaBuilder<*, *>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = BUILDERS_BY_TYPE[type] as B
    }
}
