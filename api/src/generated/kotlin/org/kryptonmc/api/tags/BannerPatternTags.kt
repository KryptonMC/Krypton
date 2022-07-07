/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Tag::class)
public object BannerPatternTags {

    // @formatter:off
    @JvmField
    public val NO_ITEM_REQUIRED: Tag<BannerPatternType> = get("no_item_required")
    @JvmField
    public val PATTERN_ITEM_FLOWER: Tag<BannerPatternType> = get("pattern_item/flower")
    @JvmField
    public val PATTERN_ITEM_CREEPER: Tag<BannerPatternType> = get("pattern_item/creeper")
    @JvmField
    public val PATTERN_ITEM_SKULL: Tag<BannerPatternType> = get("pattern_item/skull")
    @JvmField
    public val PATTERN_ITEM_MOJANG: Tag<BannerPatternType> = get("pattern_item/mojang")
    @JvmField
    public val PATTERN_ITEM_GLOBE: Tag<BannerPatternType> = get("pattern_item/globe")
    @JvmField
    public val PATTERN_ITEM_PIGLIN: Tag<BannerPatternType> = get("pattern_item/piglin")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Tag<BannerPatternType> = Krypton.tagManager[TagTypes.BANNER_PATTERNS, "minecraft:$key"]!!
}
