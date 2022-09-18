/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.util.HSVLike
import net.kyori.adventure.util.RGBLike
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton

/**
 * A standard colour encoded in RGBA (red, green, blue, alpha) format.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Color {

    /**
     * The encoded RGBA value of this colour.
     *
     * The red, green, blue, and alpha components are all encoded as 8-bit
     * unsigned bytes, where the first 8 bits is the red, the next 8 bits is
     * the green, the next 8 bits is the blue, and the last 8 bits is the
     * alpha.
     * To be exact, bits 0-7 are red, bits 8-15 are green, bits 16-23 are
     * blue, and bits 24-31 are alpha.
     */
    @get:JvmName("value")
    public val value: Int

    /**
     * The red component of this colour.
     */
    @get:JvmName("red")
    public val red: Int
        get() = value shr 16 and 0xFF

    /**
     * The green component of this colour.
     */
    @get:JvmName("green")
    public val green: Int
        get() = value shr 8 and 0xFF

    /**
     * The blue component of this colour.
     */
    @get:JvmName("blue")
    public val blue: Int
        get() = value and 0xFF

    /**
     * The alpha component of this colour.
     */
    @get:JvmName("alpha")
    public val alpha: Int
        get() = value shr 24 and 0xFF

    /**
     * Converts this colour to an Adventure RGBLike object.
     *
     * @return the converted RGBLike object
     */
    public fun asRGBLike(): RGBLike

    /**
     * Converts this colour to an Adventure HSVLike object.
     *
     * @return the converted HSVLike object
     */
    public fun asHSVLike(): HSVLike

    /**
     * Converts this colour to an Adventure text colour.
     *
     * @return the converted text colour
     */
    public fun asTextColor(): TextColor = TextColor.color(value)

    /**
     * Converts this colour to a Java AWT colour.
     *
     * @return the converted AWT colour
     * @return a new AWT colour
     */
    public fun asAwtColor(): java.awt.Color = java.awt.Color(value)

    @ApiStatus.Internal
    public interface Factory {

        public fun of(value: Int): Color

        public fun of(hue: Float, saturation: Float, brightness: Float): Color
    }

    @Suppress("UndocumentedPublicProperty")
    public companion object {

        @JvmField
        public val BLACK: Color = of(0, 0, 0)
        @JvmField
        public val WHITE: Color = of(255, 255, 255)
        @JvmField
        public val RED: Color = of(255, 0, 0)
        @JvmField
        public val GREEN: Color = of(0, 255, 0)
        @JvmField
        public val BLUE: Color = of(0, 0, 255)
        @JvmField
        public val YELLOW: Color = of(255, 255, 0)
        @JvmField
        public val MAGENTA: Color = of(255, 0, 255)
        @JvmField
        public val CYAN: Color = of(0, 255, 255)
        @JvmField
        public val GRAY: Color = of(128, 128, 128)

        /**
         * Creates a new colour with the given [value].
         *
         * @param value the RGB value
         * @return a new colour
         */
        @JvmStatic
        public fun of(value: Int): Color = Krypton.factory<Factory>().of(value)

        /**
         * Creates a new colour with the given [red], [green], and [blue] RGB
         * components.
         *
         * @param red the red component
         * @param green the green component
         * @param blue the blue component
         * @return a new colour
         */
        @JvmStatic
        @Suppress("MagicNumber")
        public fun of(red: Int, green: Int, blue: Int): Color = of(red and 255 shl 16 or (green and 255 shl 8) or (blue and 255))

        /**
         * Creates a new colour from the given [rgb] like object, or returns
         * the given [rgb] like object if it is already a colour.
         *
         * @param rgb the RGB like object
         * @return a new colour, or the given object if it is already a colour
         */
        @JvmStatic
        public fun of(rgb: RGBLike): Color {
            if (rgb is Color) return rgb
            return of(rgb.red(), rgb.green(), rgb.blue())
        }

        /**
         * Creates a new colour from the given [hue], [saturation], and
         * [brightness] components.
         *
         * @param hue the hue component
         * @param saturation the saturation component
         * @param brightness the brightness component
         * @return a new colour
         * @throws IllegalArgumentException if hue, saturation, and brightness
         * are not between 0 and 1
         */
        @JvmStatic
        public fun of(hue: Float, saturation: Float, brightness: Float): Color = Krypton.factory<Factory>().of(hue, saturation, brightness)

        /**
         * Creates a new colour from the given [hsv] like object.
         *
         * @param hsv the HSV like object
         * @return a new colour
         */
        @JvmStatic
        public fun of(hsv: HSVLike): Color = of(hsv.h(), hsv.s(), hsv.v())
    }
}
