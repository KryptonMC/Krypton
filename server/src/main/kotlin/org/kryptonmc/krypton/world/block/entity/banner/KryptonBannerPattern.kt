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
package org.kryptonmc.krypton.world.block.entity.banner

import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.enumhelper.DyeColors
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

@JvmRecord
data class KryptonBannerPattern(override val type: BannerPatternType, override val color: DyeColor) : BannerPattern {

    object Factory : BannerPattern.Factory {

        override fun of(type: BannerPatternType, color: DyeColor): BannerPattern = KryptonBannerPattern(type, color)
    }

    companion object {

        private const val PATTERN_TAG = "Pattern"
        private const val COLOR_TAG = "Color"

        @JvmStatic
        fun load(tag: CompoundTag): KryptonBannerPattern {
            val patternCode = tag.getString(PATTERN_TAG)
            val type = requireNotNull(KryptonRegistries.BANNER_PATTERN.firstOrNull { it.code == patternCode }) {
                "Could not find pattern type with code $patternCode!"
            }
            return KryptonBannerPattern(type, DyeColors.fromId(tag.getInt(COLOR_TAG)))
        }

        @JvmStatic
        fun save(pattern: BannerPattern): CompoundTag = compound {
            putString(PATTERN_TAG, pattern.type.code)
            putInt(COLOR_TAG, pattern.color.ordinal)
        }
    }
}
