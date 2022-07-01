/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.banner

/**
 * A type of banner pattern.
 *
 * @param code the shortened code identifying the banner pattern, as specified
 * by https://minecraft.fandom.com/wiki/Banner#Block_data
 */
public enum class BannerPatternType(public val code: String) {

    BASE("b"),
    TOP_LEFT_SQUARE("tl"),
    TOP_RIGHT_SQUARE("tr"),
    BOTTOM_LEFT_SQUARE("bl"),
    BOTTOM_RIGHT_SQUARE("br"),
    TOP_STRIPE("ts"),
    BOTTOM_STRIPE("bs"),
    LEFT_STRIPE("ls"),
    RIGHT_STRIPE("rs"),
    CENTER_STRIPE("cs"),
    MIDDLE_STRIPE("ms"),
    DOWN_LEFT_STRIPE("dls"),
    DOWN_RIGHT_STRIPE("drs"),
    SMALL_STRIPES("ss"),
    CROSS("cr"),
    STRAIGHT_CROSS("sc"),
    TOP_TRIANGLE("tt"),
    BOTTOM_TRIANGLE("bt"),
    TOP_TRIANGLES("tts"),
    BOTTOM_TRIANGLES("bts"),
    LEFT_DIAGONAL("ld"),
    RIGHT_DIAGONAL("rd"),
    LEFT_REVERSE_DIAGONAL("lud"),
    RIGHT_REVERSE_DIAGONAL("rud"),
    MIDDLE_CIRCLE("mc"),
    MIDDLE_RHOMBUS("mr"),
    TOP_HALF_HORIZONTAL("hh"),
    BOTTOM_HALF_HORIZONTAL("hhb"),
    LEFT_HALF_VERTICAL("vh"),
    RIGHT_HALF_VERTICAL("vhr"),
    BORDER("bo"),
    CURLY_BORDER("cbo"),
    GRADIENT("gra"),
    REVERSE_GRADIENT("gru"),
    BRICKS("bri"),
    GLOBE("glb"),
    CREEPER("cre"),
    SKULL("sku"),
    FLOWER("flo"),
    MOJANG("moj"),
    PIGLIN("pig");
}
