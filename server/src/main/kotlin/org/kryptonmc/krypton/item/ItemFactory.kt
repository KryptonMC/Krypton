/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        ItemTypes.WRITTEN_BOOK.get() to Function { KryptonWrittenBookMeta(it) },
        ItemTypes.WRITABLE_BOOK.get() to Function { KryptonWritableBookMeta(it) },
        ItemTypes.PLAYER_HEAD.get() to Function { KryptonPlayerHeadMeta(it) },
        ItemTypes.LEATHER_HELMET.get() to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_CHESTPLATE.get() to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_LEGGINGS.get() to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_BOOTS.get() to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.LEATHER_HORSE_ARMOR.get() to Function { KryptonLeatherArmorMeta(it) },
        ItemTypes.FIREWORK_ROCKET.get() to Function { KryptonFireworkRocketMeta(it) },
        ItemTypes.FIREWORK_STAR.get() to Function { KryptonFireworkStarMeta(it) },
        ItemTypes.CROSSBOW.get() to Function { KryptonCrossbowMeta(it) },
        ItemTypes.COMPASS.get() to Function { KryptonCompassMeta(it) },
        ItemTypes.BUNDLE.get() to Function { KryptonBundleMeta(it) },
        ItemTypes.WHITE_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.ORANGE_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.MAGENTA_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.LIGHT_BLUE_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.YELLOW_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.LIME_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.PINK_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.GRAY_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.LIGHT_GRAY_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.CYAN_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.PURPLE_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.BLUE_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.BROWN_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.GREEN_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.RED_BANNER.get() to Function { KryptonBannerMeta(it) },
        ItemTypes.BLACK_BANNER.get() to Function { KryptonBannerMeta(it) }
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
