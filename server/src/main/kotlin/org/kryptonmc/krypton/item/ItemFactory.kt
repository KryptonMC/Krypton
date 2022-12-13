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
package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.BannerMeta
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
import org.kryptonmc.krypton.item.meta.KryptonBannerMeta
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
import java.util.function.Function
import java.util.function.Supplier

object ItemFactory {

    private val META_BY_TYPE: Map<ItemType, Function<CompoundTag, AbstractItemMeta<*>>> = mapOf(
        ItemTypes.WRITTEN_BOOK to Function { KryptonWrittenBookMeta(it) },
        ItemTypes.WRITABLE_BOOK to Function { KryptonWritableBookMeta(it) },
        ItemTypes.PLAYER_HEAD to Function { KryptonPlayerHeadMeta(it) },
        ItemTypes.LEATHER_HELMET to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_CHESTPLATE to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_LEGGINGS to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_BOOTS to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_HORSE_ARMOR to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.FIREWORK_ROCKET to Function { KryptonFireworkRocketMeta(it) },
        ItemTypes.FIREWORK_STAR to Function { KryptonFireworkStarMeta(it) },
        ItemTypes.CROSSBOW to Function { KryptonCrossbowMeta(it) },
        ItemTypes.COMPASS to Function { KryptonCompassMeta(it) },
        ItemTypes.BUNDLE to Function { KryptonBundleMeta(it) },
        ItemTypes.WHITE_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.ORANGE_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.MAGENTA_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.LIGHT_BLUE_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.YELLOW_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.LIME_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.PINK_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.GRAY_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.LIGHT_GRAY_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.CYAN_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.PURPLE_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.BLUE_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.BROWN_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.GREEN_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.RED_BANNER to Function { KryptonBannerMeta(it) },
        ItemTypes.BLACK_BANNER to Function { KryptonBannerMeta(it) }
    )
    private val BUILDERS_BY_TYPE: Map<Class<out ItemMetaBuilder.Provider<*>>, Supplier<ItemMetaBuilder<*, *>>> = mapOf(
        BannerMeta::class.java to Supplier { KryptonBannerMeta.Builder() },
        BundleMeta::class.java to Supplier { KryptonBundleMeta.Builder() },
        CompassMeta::class.java to Supplier { KryptonCompassMeta.Builder() },
        CrossbowMeta::class.java to Supplier { KryptonCrossbowMeta.Builder() },
        FireworkRocketMeta::class.java to Supplier { KryptonFireworkRocketMeta.Builder() },
        FireworkStarMeta::class.java to Supplier { KryptonFireworkStarMeta.Builder() },
        LeatherArmorMeta::class.java to Supplier { KryptonLeatherArmorMeta.Builder() },
        PlayerHeadMeta::class.java to Supplier { KryptonPlayerHeadMeta.Builder() },
        WritableBookMeta::class.java to Supplier { KryptonWritableBookMeta.Builder() },
        WrittenBookMeta::class.java to Supplier { KryptonWrittenBookMeta.Builder() }
    )

    @JvmStatic
    fun create(type: ItemType, data: CompoundTag): AbstractItemMeta<*> = META_BY_TYPE.get(type)?.apply(data) ?: KryptonItemMeta(data)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = BUILDERS_BY_TYPE.get(type)?.get() as B
}
