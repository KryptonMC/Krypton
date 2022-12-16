/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.resource.ResourceKeys

/**
 * This file is auto-generated. Do not edit this manually!
 */
public object BannerPatternTags {

    // @formatter:off
    @JvmField
    public val NO_ITEM_REQUIRED: TagKey<BannerPatternType> = get("no_item_required")
    @JvmField
    public val PATTERN_ITEM_FLOWER: TagKey<BannerPatternType> = get("pattern_item/flower")
    @JvmField
    public val PATTERN_ITEM_CREEPER: TagKey<BannerPatternType> = get("pattern_item/creeper")
    @JvmField
    public val PATTERN_ITEM_SKULL: TagKey<BannerPatternType> = get("pattern_item/skull")
    @JvmField
    public val PATTERN_ITEM_MOJANG: TagKey<BannerPatternType> = get("pattern_item/mojang")
    @JvmField
    public val PATTERN_ITEM_GLOBE: TagKey<BannerPatternType> = get("pattern_item/globe")
    @JvmField
    public val PATTERN_ITEM_PIGLIN: TagKey<BannerPatternType> = get("pattern_item/piglin")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): TagKey<BannerPatternType> = TagKey.of(ResourceKeys.BANNER_PATTERN, Key.key(key))
}
