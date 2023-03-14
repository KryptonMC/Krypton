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
package org.kryptonmc.api.block.entity.banner

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla banner pattern types.
 */
@Catalogue(BannerPatternType::class)
public object BannerPatternTypes {

    @JvmField
    public val BASE: RegistryReference<BannerPatternType> = of("base")
    @JvmField
    public val BOTTOM_LEFT_SQUARE: RegistryReference<BannerPatternType> = of("square_bottom_left")
    @JvmField
    public val BOTTOM_RIGHT_SQUARE: RegistryReference<BannerPatternType> = of("square_bottom_right")
    @JvmField
    public val TOP_LEFT_SQUARE: RegistryReference<BannerPatternType> = of("square_top_left")
    @JvmField
    public val TOP_RIGHT_SQUARE: RegistryReference<BannerPatternType> = of("square_top_right")
    @JvmField
    public val BOTTOM_STRIPE: RegistryReference<BannerPatternType> = of("stripe_bottom")
    @JvmField
    public val TOP_STRIPE: RegistryReference<BannerPatternType> = of("stripe_top")
    @JvmField
    public val LEFT_STRIPE: RegistryReference<BannerPatternType> = of("stripe_left")
    @JvmField
    public val RIGHT_STRIPE: RegistryReference<BannerPatternType> = of("stripe_right")
    @JvmField
    public val CENTER_STRIPE: RegistryReference<BannerPatternType> = of("stripe_center")
    @JvmField
    public val MIDDLE_STRIPE: RegistryReference<BannerPatternType> = of("stripe_middle")
    @JvmField
    public val DOWN_RIGHT_STRIPE: RegistryReference<BannerPatternType> = of("stripe_downright")
    @JvmField
    public val DOWN_LEFT_STRIPE: RegistryReference<BannerPatternType> = of("stripe_downleft")
    @JvmField
    public val SMALL_STRIPES: RegistryReference<BannerPatternType> = of("small_stripes")
    @JvmField
    public val CROSS: RegistryReference<BannerPatternType> = of("cross")
    @JvmField
    public val STRAIGHT_CROSS: RegistryReference<BannerPatternType> = of("straight_cross")
    @JvmField
    public val BOTTOM_TRIANGLE: RegistryReference<BannerPatternType> = of("triangle_bottom")
    @JvmField
    public val TOP_TRIANGLE: RegistryReference<BannerPatternType> = of("triangle_top")
    @JvmField
    public val BOTTOM_TRIANGLES: RegistryReference<BannerPatternType> = of("triangles_bottom")
    @JvmField
    public val TOP_TRIANGLES: RegistryReference<BannerPatternType> = of("triangles_top")
    @JvmField
    public val LEFT_DIAGONAL: RegistryReference<BannerPatternType> = of("diagonal_left")
    @JvmField
    public val RIGHT_DIAGONAL: RegistryReference<BannerPatternType> = of("diagonal_up_right")
    @JvmField
    public val LEFT_REVERSE_DIAGONAL: RegistryReference<BannerPatternType> = of("diagonal_up_left")
    @JvmField
    public val RIGHT_REVERSE_DIAGONAL: RegistryReference<BannerPatternType> = of("diagonal_right")
    @JvmField
    public val MIDDLE_CIRCLE: RegistryReference<BannerPatternType> = of("circle")
    @JvmField
    public val MIDDLE_RHOMBUS: RegistryReference<BannerPatternType> = of("rhombus")
    @JvmField
    public val LEFT_HALF_VERTICAL: RegistryReference<BannerPatternType> = of("half_vertical")
    @JvmField
    public val TOP_HALF_HORIZONTAL: RegistryReference<BannerPatternType> = of("half_horizontal")
    @JvmField
    public val RIGHT_HALF_VERTICAL: RegistryReference<BannerPatternType> = of("half_vertical_right")
    @JvmField
    public val BOTTOM_HALF_HORIZONTAL: RegistryReference<BannerPatternType> = of("half_horizontal_bottom")
    @JvmField
    public val BORDER: RegistryReference<BannerPatternType> = of("border")
    @JvmField
    public val CURLY_BORDER: RegistryReference<BannerPatternType> = of("curly_border")
    @JvmField
    public val GRADIENT: RegistryReference<BannerPatternType> = of("gradient")
    @JvmField
    public val REVERSE_GRADIENT: RegistryReference<BannerPatternType> = of("gradient_up")
    @JvmField
    public val BRICKS: RegistryReference<BannerPatternType> = of("bricks")
    @JvmField
    public val GLOBE: RegistryReference<BannerPatternType> = of("globe")
    @JvmField
    public val CREEPER: RegistryReference<BannerPatternType> = of("creeper")
    @JvmField
    public val SKULL: RegistryReference<BannerPatternType> = of("skull")
    @JvmField
    public val FLOWER: RegistryReference<BannerPatternType> = of("flower")
    @JvmField
    public val MOJANG: RegistryReference<BannerPatternType> = of("mojang")
    @JvmField
    public val PIGLIN: RegistryReference<BannerPatternType> = of("piglin")

    @JvmStatic
    private fun of(name: String): RegistryReference<BannerPatternType> = RegistryReference.of(Registries.BANNER_PATTERN, Key.key(name))
}
