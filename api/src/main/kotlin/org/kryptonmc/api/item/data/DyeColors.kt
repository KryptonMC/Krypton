/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla dye colours.
 */
@Catalogue(DyeColor::class)
public object DyeColors {

    // @formatter:off
    @JvmField
    public val WHITE: DyeColor = get("white")
    @JvmField
    public val ORANGE: DyeColor = get("orange")
    @JvmField
    public val MAGENTA: DyeColor = get("magenta")
    @JvmField
    public val LIGHT_BLUE: DyeColor = get("light_blue")
    @JvmField
    public val YELLOW: DyeColor = get("yellow")
    @JvmField
    public val LIME: DyeColor = get("lime")
    @JvmField
    public val PINK: DyeColor = get("pink")
    @JvmField
    public val GRAY: DyeColor = get("gray")
    @JvmField
    public val LIGHT_GRAY: DyeColor = get("light_gray")
    @JvmField
    public val CYAN: DyeColor = get("cyan")
    @JvmField
    public val PURPLE: DyeColor = get("purple")
    @JvmField
    public val BLUE: DyeColor = get("blue")
    @JvmField
    public val BROWN: DyeColor = get("brown")
    @JvmField
    public val GREEN: DyeColor = get("green")
    @JvmField
    public val RED: DyeColor = get("red")
    @JvmField
    public val BLACK: DyeColor = get("black")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): DyeColor = Registries.DYE_COLORS.get(Key.key(name))!!
}
