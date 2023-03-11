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
