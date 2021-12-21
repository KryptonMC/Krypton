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
package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.BundleMeta
import org.kryptonmc.api.item.meta.CompassMeta
import org.kryptonmc.api.item.meta.CrossbowMeta
import org.kryptonmc.api.item.meta.FireworkRocketMeta
import org.kryptonmc.api.item.meta.FireworkStarMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.api.item.meta.LeatherArmorMeta
import org.kryptonmc.api.item.meta.PlayerHeadMeta
import org.kryptonmc.api.item.meta.WritableBookMeta
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.item.meta.AbstractItemMeta
import org.kryptonmc.krypton.item.meta.KryptonBundleMeta
import org.kryptonmc.krypton.item.meta.KryptonCompassMeta
import org.kryptonmc.krypton.item.meta.KryptonCrossbowMeta
import org.kryptonmc.krypton.item.meta.KryptonFireworkRocketMeta
import org.kryptonmc.krypton.item.meta.KryptonFireworkStarMeta
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.item.meta.KryptonLeatherArmorMeta
import org.kryptonmc.krypton.item.meta.KryptonPlayerHeadMeta
import org.kryptonmc.krypton.item.meta.KryptonWritableBookMeta
import org.kryptonmc.krypton.item.meta.KryptonWrittenBookMeta
import org.kryptonmc.nbt.CompoundTag

object ItemFactory {

    private val META_BY_TYPE: Map<ItemType, (CompoundTag) -> AbstractItemMeta<*>> = mapOf(
        ItemTypes.WRITTEN_BOOK to ::KryptonWrittenBookMeta,
        ItemTypes.WRITABLE_BOOK to ::KryptonWritableBookMeta,
        ItemTypes.PLAYER_HEAD to ::KryptonPlayerHeadMeta,
        ItemTypes.LEATHER_HELMET to ::KryptonLeatherArmorMeta,
        ItemTypes.LEATHER_CHESTPLATE to ::KryptonLeatherArmorMeta,
        ItemTypes.LEATHER_LEGGINGS to ::KryptonLeatherArmorMeta,
        ItemTypes.LEATHER_BOOTS to ::KryptonLeatherArmorMeta,
        ItemTypes.LEATHER_HORSE_ARMOR to ::KryptonLeatherArmorMeta,
        ItemTypes.FIREWORK_ROCKET to ::KryptonFireworkRocketMeta,
        ItemTypes.FIREWORK_STAR to ::KryptonFireworkStarMeta,
        ItemTypes.CROSSBOW to ::KryptonCrossbowMeta,
        ItemTypes.COMPASS to ::KryptonCompassMeta,
        ItemTypes.BUNDLE to ::KryptonBundleMeta,
    )
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
    fun create(type: ItemType, tag: CompoundTag): AbstractItemMeta<*> = META_BY_TYPE[type]?.invoke(tag) ?: KryptonItemMeta(tag)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = BUILDERS_BY_TYPE[type]?.invoke() as B
}
