/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map.color

import java.awt.Color

/**
 * The colour of a pixel on a map.
 *
 * @param color the base RGB colour, without shading
 */
public enum class MapColorType(public val color: Color) {

    NONE(Color(0)),
    GRASS(Color(8368696)),
    SAND(Color(16247203)),
    WOOL(Color(13092807)),
    FIRE(Color(16711680)),
    ICE(Color(10526975)),
    METAL(Color(10987431)),
    PLANT(Color(31744)),
    SNOW(Color(16777215)),
    CLAY(Color(10791096)),
    DIRT(Color(9923917)),
    STONE(Color(7368816)),
    WATER(Color(4210943)),
    WOOD(Color(9402184)),
    QUARTZ(Color(16776437)),
    COLOR_ORANGE(Color(14188339)),
    COLOR_MAGENTA(Color(11685080)),
    COLOR_LIGHT_BLUE(Color(6724056)),
    COLOR_YELLOW(Color(15066419)),
    COLOR_LIGHT_GREEN(Color(8375321)),
    COLOR_PINK(Color(15892389)),
    COLOR_GRAY(Color(5000268)),
    COLOR_LIGHT_GRAY(Color(10066329)),
    COLOR_CYAN(Color(5013401)),
    COLOR_PURPLE(Color(8339378)),
    COLOR_BLUE(Color(3361970)),
    COLOR_BROWN(Color(6704179)),
    COLOR_GREEN(Color(6717235)),
    COLOR_RED(Color(10040115)),
    COLOR_BLACK(Color(1644825)),
    GOLD(Color(16445005)),
    DIAMOND(Color(6085589)),
    LAPIS(Color(4882687)),
    EMERALD(Color(55610)),
    PODZOL(Color(8476209)),
    NETHER(Color(7340544)),
    TERRACOTTA_WHITE(Color(13742497)),
    TERRACOTTA_ORANGE(Color(10441252)),
    TERRACOTTA_MAGENTA(Color(9787244)),
    TERRACOTTA_LIGHT_BLUE(Color(7367818)),
    TERRACOTTA_YELLOW(Color(12223780)),
    TERRACOTTA_LIGHT_GREEN(Color(6780213)),
    TERRACOTTA_PINK(Color(10505550)),
    TERRACOTTA_GRAY(Color(3746083)),
    TERRACOTTA_LIGHT_GRAY(Color(8874850)),
    TERRACOTTA_CYAN(Color(5725276)),
    TERRACOTTA_PURPLE(Color(8014168)),
    TERRACOTTA_BLUE(Color(4996700)),
    TERRACOTTA_BROWN(Color(4993571)),
    TERRACOTTA_GREEN(Color(5001770)),
    TERRACOTTA_RED(Color(9321518)),
    TERRACOTTA_BLACK(Color(2430480)),
    CRIMSON_NYLIUM(Color(12398641)),
    CRIMSON_STEM(Color(9715553)),
    CRIMSON_HYPHAE(Color(6035741)),
    WARPED_NYLIUM(Color(1474182)),
    WARPED_STEM(Color(3837580)),
    WARPED_HYPHAE(Color(5647422)),
    WARPED_WART_BLOCK(Color(1356933)),
    DEEPSLATE(Color(6579300)),
    RAW_IRON(Color(14200723)),
    GLOW_LICHEN(Color(8365974))
}
