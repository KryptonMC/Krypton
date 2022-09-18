/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.banner

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla banner pattern types.
 */
@Catalogue(BannerPatternType::class)
public object BannerPatternTypes {

    @JvmField
    public val BASE: BannerPatternType = get("base")
    @JvmField
    public val BOTTOM_LEFT_SQUARE: BannerPatternType = get("square_bottom_left")
    @JvmField
    public val BOTTOM_RIGHT_SQUARE: BannerPatternType = get("square_bottom_right")
    @JvmField
    public val TOP_LEFT_SQUARE: BannerPatternType = get("square_top_left")
    @JvmField
    public val TOP_RIGHT_SQUARE: BannerPatternType = get("square_top_right")
    @JvmField
    public val BOTTOM_STRIPE: BannerPatternType = get("stripe_bottom")
    @JvmField
    public val TOP_STRIPE: BannerPatternType = get("stripe_top")
    @JvmField
    public val LEFT_STRIPE: BannerPatternType = get("stripe_left")
    @JvmField
    public val RIGHT_STRIPE: BannerPatternType = get("stripe_right")
    @JvmField
    public val CENTER_STRIPE: BannerPatternType = get("stripe_center")
    @JvmField
    public val MIDDLE_STRIPE: BannerPatternType = get("stripe_middle")
    @JvmField
    public val DOWN_RIGHT_STRIPE: BannerPatternType = get("stripe_downright")
    @JvmField
    public val DOWN_LEFT_STRIPE: BannerPatternType = get("stripe_downleft")
    @JvmField
    public val SMALL_STRIPES: BannerPatternType = get("small_stripes")
    @JvmField
    public val CROSS: BannerPatternType = get("cross")
    @JvmField
    public val STRAIGHT_CROSS: BannerPatternType = get("straight_cross")
    @JvmField
    public val BOTTOM_TRIANGLE: BannerPatternType = get("triangle_bottom")
    @JvmField
    public val TOP_TRIANGLE: BannerPatternType = get("triangle_top")
    @JvmField
    public val BOTTOM_TRIANGLES: BannerPatternType = get("triangles_bottom")
    @JvmField
    public val TOP_TRIANGLES: BannerPatternType = get("triangles_top")
    @JvmField
    public val LEFT_DIAGONAL: BannerPatternType = get("diagonal_left")
    @JvmField
    public val RIGHT_DIAGONAL: BannerPatternType = get("diagonal_up_right")
    @JvmField
    public val LEFT_REVERSE_DIAGONAL: BannerPatternType = get("diagonal_up_left")
    @JvmField
    public val RIGHT_REVERSE_DIAGONAL: BannerPatternType = get("diagonal_right")
    @JvmField
    public val MIDDLE_CIRCLE: BannerPatternType = get("circle")
    @JvmField
    public val MIDDLE_RHOMBUS: BannerPatternType = get("rhombus")
    @JvmField
    public val LEFT_HALF_VERTICAL: BannerPatternType = get("half_vertical")
    @JvmField
    public val TOP_HALF_HORIZONTAL: BannerPatternType = get("half_horizontal")
    @JvmField
    public val RIGHT_HALF_VERTICAL: BannerPatternType = get("half_vertical_right")
    @JvmField
    public val BOTTOM_HALF_HORIZONTAL: BannerPatternType = get("half_horizontal_bottom")
    @JvmField
    public val BORDER: BannerPatternType = get("border")
    @JvmField
    public val CURLY_BORDER: BannerPatternType = get("curly_border")
    @JvmField
    public val GRADIENT: BannerPatternType = get("gradient")
    @JvmField
    public val REVERSE_GRADIENT: BannerPatternType = get("gradient_up")
    @JvmField
    public val BRICKS: BannerPatternType = get("bricks")
    @JvmField
    public val GLOBE: BannerPatternType = get("globe")
    @JvmField
    public val CREEPER: BannerPatternType = get("creeper")
    @JvmField
    public val SKULL: BannerPatternType = get("skull")
    @JvmField
    public val FLOWER: BannerPatternType = get("flower")
    @JvmField
    public val MOJANG: BannerPatternType = get("mojang")
    @JvmField
    public val PIGLIN: BannerPatternType = get("piglin")

    @JvmStatic
    private fun get(name: String): BannerPatternType = Registries.BANNER_PATTERN.get(Key.key(name))!!
}
