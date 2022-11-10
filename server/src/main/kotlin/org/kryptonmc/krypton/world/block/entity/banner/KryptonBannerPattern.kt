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
package org.kryptonmc.krypton.world.block.entity.banner

import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.DyeColors
import org.kryptonmc.nbt.CompoundTag

@JvmRecord
data class KryptonBannerPattern(override val type: BannerPatternType, override val color: DyeColor) : BannerPattern {

    object Factory : BannerPattern.Factory {

        override fun of(type: BannerPatternType, color: DyeColor): BannerPattern = KryptonBannerPattern(type, color)
    }

    companion object {

        @JvmStatic
        fun from(tag: CompoundTag): KryptonBannerPattern {
            val patternCode = tag.getString("Pattern")
            val type = requireNotNull(KryptonRegistries.BANNER_PATTERN.firstOrNull { it.code == patternCode }) {
                "Could not find pattern type with code $patternCode!"
            }
            return KryptonBannerPattern(type, DyeColors.fromId(tag.getInt("Color")))
        }
    }
}
