package org.kryptonmc.krypton.world.block.entity.banner

import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.item.data.DyeColor

@JvmRecord
data class KryptonBannerPattern(
    override val type: BannerPatternType,
    override val color: DyeColor
) : BannerPattern {

    object Factory : BannerPattern.Factory {

        override fun of(type: BannerPatternType, color: DyeColor): BannerPattern = KryptonBannerPattern(type, color)
    }
}
