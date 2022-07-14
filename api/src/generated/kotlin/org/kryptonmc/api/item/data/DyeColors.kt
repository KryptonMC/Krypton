/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

import kotlin.Int
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.util.Color

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(DyeColor::class)
public object DyeColors {

    // @formatter:off
    @JvmField
    public val WHITE: DyeColor = register("white", Color.of(16777215), Color.of(15790320), 16777215)
    @JvmField
    public val ORANGE: DyeColor = register("orange", Color.of(14188339), Color.of(15435844), 16738335)
    @JvmField
    public val MAGENTA: DyeColor = register("magenta", Color.of(11685080), Color.of(12801229), 16711935)
    @JvmField
    public val LIGHT_BLUE: DyeColor = register("light_blue", Color.of(6724056), Color.of(6719955), 10141901)
    @JvmField
    public val YELLOW: DyeColor = register("yellow", Color.of(15066419), Color.of(14602026), 16776960)
    @JvmField
    public val LIME: DyeColor = register("lime", Color.of(8375321), Color.of(4312372), 12582656)
    @JvmField
    public val PINK: DyeColor = register("pink", Color.of(15892389), Color.of(14188952), 16738740)
    @JvmField
    public val GRAY: DyeColor = register("gray", Color.of(5000268), Color.of(4408131), 8421504)
    @JvmField
    public val LIGHT_GRAY: DyeColor = register("light_gray", Color.of(10066329), Color.of(11250603), 13882323)
    @JvmField
    public val CYAN: DyeColor = register("cyan", Color.of(5013401), Color.of(2651799), 65535)
    @JvmField
    public val PURPLE: DyeColor = register("purple", Color.of(8339378), Color.of(8073150), 10494192)
    @JvmField
    public val BLUE: DyeColor = register("blue", Color.of(3361970), Color.of(2437522), 255)
    @JvmField
    public val BROWN: DyeColor = register("brown", Color.of(6704179), Color.of(5320730), 9127187)
    @JvmField
    public val GREEN: DyeColor = register("green", Color.of(6717235), Color.of(3887386), 65280)
    @JvmField
    public val RED: DyeColor = register("red", Color.of(10040115), Color.of(11743532), 16711680)
    @JvmField
    public val BLACK: DyeColor = register("black", Color.of(1644825), Color.of(1973019), 0)

    // @formatter:on
    @JvmStatic
    private fun register(
        name: String,
        color: Color,
        fireworkColor: Color,
        textColor: Int,
    ): DyeColor {
        val key = Key.key(name)
        return Registries.DYE_COLORS.register(key, DyeColor.of(key, color, fireworkColor, TextColor.color(textColor)))
    }
}
