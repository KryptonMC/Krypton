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
    public val BASE: BannerPatternType = register("base", "b")
    @JvmField
    public val BOTTOM_LEFT_SQUARE: BannerPatternType = register("square_bottom_left", "bl")
    @JvmField
    public val BOTTOM_RIGHT_SQUARE: BannerPatternType = register("square_bottom_right", "br")
    @JvmField
    public val TOP_LEFT_SQUARE: BannerPatternType = register("square_top_left", "tl")
    @JvmField
    public val TOP_RIGHT_SQUARE: BannerPatternType = register("square_top_right", "tr")
    @JvmField
    public val BOTTOM_STRIPE: BannerPatternType = register("stripe_bottom", "bs")
    @JvmField
    public val TOP_STRIPE: BannerPatternType = register("stripe_top", "ts")
    @JvmField
    public val LEFT_STRIPE: BannerPatternType = register("stripe_left", "ls")
    @JvmField
    public val RIGHT_STRIPE: BannerPatternType = register("stripe_right", "rs")
    @JvmField
    public val CENTER_STRIPE: BannerPatternType = register("stripe_center", "cs")
    @JvmField
    public val MIDDLE_STRIPE: BannerPatternType = register("stripe_middle", "ms")
    @JvmField
    public val DOWN_RIGHT_STRIPE: BannerPatternType = register("stripe_downright", "drs")
    @JvmField
    public val DOWN_LEFT_STRIPE: BannerPatternType = register("stripe_downleft", "dls")
    @JvmField
    public val SMALL_STRIPES: BannerPatternType = register("small_stripes", "ss")
    @JvmField
    public val CROSS: BannerPatternType = register("cross", "cr")
    @JvmField
    public val STRAIGHT_CROSS: BannerPatternType = register("straight_cross", "sc")
    @JvmField
    public val BOTTOM_TRIANGLE: BannerPatternType = register("triangle_bottom", "bt")
    @JvmField
    public val TOP_TRIANGLE: BannerPatternType = register("triangle_top", "tt")
    @JvmField
    public val BOTTOM_TRIANGLES: BannerPatternType = register("triangles_bottom", "bts")
    @JvmField
    public val TOP_TRIANGLES: BannerPatternType = register("triangles_top", "tts")
    @JvmField
    public val LEFT_DIAGONAL: BannerPatternType = register("diagonal_left", "ld")
    @JvmField
    public val RIGHT_DIAGONAL: BannerPatternType = register("diagonal_up_right", "rd")
    @JvmField
    public val LEFT_REVERSE_DIAGONAL: BannerPatternType = register("diagonal_up_left", "lud")
    @JvmField
    public val RIGHT_REVERSE_DIAGONAL: BannerPatternType = register("diagonal_right", "rud")
    @JvmField
    public val MIDDLE_CIRCLE: BannerPatternType = register("circle", "mc")
    @JvmField
    public val MIDDLE_RHOMBUS: BannerPatternType = register("rhombus", "mr")
    @JvmField
    public val LEFT_HALF_VERTICAL: BannerPatternType = register("half_vertical", "vh")
    @JvmField
    public val TOP_HALF_HORIZONTAL: BannerPatternType = register("half_horizontal", "hh")
    @JvmField
    public val RIGHT_HALF_VERTICAL: BannerPatternType = register("half_vertical_right", "vhr")
    @JvmField
    public val BOTTOM_HALF_HORIZONTAL: BannerPatternType = register("half_horizontal_bottom", "hhb")
    @JvmField
    public val BORDER: BannerPatternType = register("border", "bo")
    @JvmField
    public val CURLY_BORDER: BannerPatternType = register("curly_border", "cbo")
    @JvmField
    public val GRADIENT: BannerPatternType = register("gradient", "gra")
    @JvmField
    public val REVERSE_GRADIENT: BannerPatternType = register("gradient_up", "gru")
    @JvmField
    public val BRICKS: BannerPatternType = register("bricks", "bri")
    @JvmField
    public val GLOBE: BannerPatternType = register("globe", "glb")
    @JvmField
    public val CREEPER: BannerPatternType = register("creeper", "cre")
    @JvmField
    public val SKULL: BannerPatternType = register("skull", "sku")
    @JvmField
    public val FLOWER: BannerPatternType = register("flower", "flo")
    @JvmField
    public val MOJANG: BannerPatternType = register("mojang", "moj")
    @JvmField
    public val PIGLIN: BannerPatternType = register("piglin", "pig")

    @JvmStatic
    private fun register(name: String, code: String): BannerPatternType {
        val key = Key.key(name)
        return Registries.BANNER_PATTERN.register(key, BannerPatternType.of(key, code))
    }
}
