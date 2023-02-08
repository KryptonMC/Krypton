/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.api.util.Color

/**
 * A colour of a dye.
 *
 * @param color the main colour of the dye colour
 * @param fireworkColor the firework colour of the dye colour
 * @param textColor the text colour of the dye colour
 */
@Suppress("MagicNumber")
public enum class DyeColor(public val color: Color, public val fireworkColor: Color, public val textColor: TextColor) {

    WHITE(Color(0xFFFFFF), Color(0xF0F0F0), NamedTextColor.WHITE),
    ORANGE(Color(0xD87F33), Color(0xEB8844), TextColor.color(0xFF681F)),
    MAGENTA(Color(0xB24CD8), Color(0xC354CD), TextColor.color(0xFF00FF)),
    LIGHT_BLUE(Color(0x6699D8), Color(0x6689D3), TextColor.color(0x9AC0CD)),
    YELLOW(Color(0xE5E533), Color(0xDECF2A), TextColor.color(0xFFFF00)),
    LIME(Color(0x7FCC19), Color(0x41CD34), TextColor.color(0xBFFF00)),
    PINK(Color(0xF27FA5), Color(0xD88198), TextColor.color(0xFF69B4)),
    GRAY(Color(0x4C4C4C), Color(0x434343), TextColor.color(0x808080)),
    LIGHT_GRAY(Color(0x999999), Color(0xABABAB), TextColor.color(0xD3D3D3)),
    CYAN(Color(0x4C7F99), Color(0x287697), TextColor.color(0x00FFFF)),
    PURPLE(Color(0x7F3FB2), Color(0x7B2FBE), TextColor.color(0xA020F0)),
    BLUE(Color(0x334CB2), Color(0x253192), TextColor.color(0x0000FF)),
    BROWN(Color(0x664C33), Color(0x51301A), TextColor.color(0x8B4513)),
    GREEN(Color(0x667F33), Color(0x3B511A), TextColor.color(0x00FF00)),
    RED(Color(0x993333), Color(0xB3312C), TextColor.color(0xFF0000)),
    BLACK(Color(0x191919), Color(0x1E1B1B), NamedTextColor.BLACK);
}
