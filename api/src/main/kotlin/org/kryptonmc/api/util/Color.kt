/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.util.HSVLike
import net.kyori.adventure.util.RGBLike
import kotlin.math.floor

/**
 * A standard colour in RGB format, with a red, green, and blue component.
 *
 * @property red The red component.
 * @property green The green component.
 * @property blue The blue component.
 */
@JvmRecord
@Suppress("MagicNumber")
public data class Color(
    @JvmSynthetic
    @get:JvmName("red-impl")
    public val red: Int,
    @JvmSynthetic
    @get:JvmName("green-impl")
    public val green: Int,
    @JvmSynthetic
    @get:JvmName("blue-impl")
    public val blue: Int
) : RGBLike {

    init {
        require(red >= 0 && red <= 255) { "Red must be between 0 and 255, was $red!" }
        require(green >= 0 && green <= 255) { "Green must be between 0 and 255, was $green!" }
        require(blue >= 0 && blue <= 255) { "Blue must be between 0 and 255, was $blue!" }
    }

    /**
     * Creates a new colour from the given encoded RGB [value].
     *
     * @param value the encoded RGB value
     */
    public constructor(value: Int) : this(value shr 16 and 0xFF, value shr 8 and 0xFF, value and 0xFF)

    /**
     * Gets this colour in encoded RGB form.
     *
     * The encoded form of an RGB colour is defined as follows:
     * - Bits 0-7 are the blue component
     * - Bits 8-15 are the green component
     * - Bits 16-23 are the red component
     * - Bits 24-31 are unused
     *
     * @return the encoded RGB value
     */
    public fun encode(): Int {
        return (red shl 16) or (green shl 8) or (blue shl 0)
    }

    /**
     * Creates a new colour with the given new [red] value.
     *
     * @param red the new red value
     * @return the new colour
     */
    public fun withRed(red: Int): Color = Color(red, this.green, this.blue)

    /**
     * Creates a new colour with the given new [green] value.
     *
     * @param green the new green value
     * @return the new colour
     */
    public fun withGreen(green: Int): Color = Color(this.red, green, this.blue)

    /**
     * Creates a new colour with the given new [blue] value.
     *
     * @param blue the new blue value
     * @return the new colour
     */
    public fun withBlue(blue: Int): Color = Color(this.red, this.green, blue)

    override fun red(): Int = red

    override fun green(): Int = green

    override fun blue(): Int = blue

    @Suppress("UndocumentedPublicProperty")
    public companion object {

        @JvmField
        public val BLACK: Color = Color(0, 0, 0)
        @JvmField
        public val WHITE: Color = Color(255, 255, 255)
        @JvmField
        public val RED: Color = Color(255, 0, 0)
        @JvmField
        public val GREEN: Color = Color(0, 255, 0)
        @JvmField
        public val BLUE: Color = Color(0, 0, 255)
        @JvmField
        public val YELLOW: Color = Color(255, 255, 0)
        @JvmField
        public val MAGENTA: Color = Color(255, 0, 255)
        @JvmField
        public val CYAN: Color = Color(0, 255, 255)
        @JvmField
        public val GRAY: Color = Color(128, 128, 128)

        /**
         * Creates a new colour from the given [hue], [saturation],
         * and [value] components.
         *
         * @param hue the hue, between 0 and 1
         * @param saturation the saturation, between 0 and 1
         * @param value the value, between 0 and 1
         * @return the new colour
         */
        @JvmStatic
        public fun fromHsv(hue: Float, saturation: Float, value: Float): Color {
            // This is taken from java.awt.Color#HSBtoRGB
            if (saturation == 0F) {
                val result = (value * 255F + 0.5F).toInt()
                return Color(result, result, result)
            }

            val h = (hue - floor(hue)) * 6F
            val f = h - floor(h)
            val p = value * (1F - saturation)
            val q = value * (1F - saturation * f)
            val t = value * (1F - (saturation * (1F - f)))

            return when (h.toInt()) {
                0 -> Color((value * 255F + 0.5F).toInt(), (t * 255F + 0.5F).toInt(), (p * 255F + 0.5F).toInt())
                1 -> Color((q * 255F + 0.5F).toInt(), (value * 255F + 0.5F).toInt(), (p * 255F + 0.5F).toInt())
                2 -> Color((p * 255F + 0.5F).toInt(), (value * 255F + 0.5F).toInt(), (t * 255F + 0.5F).toInt())
                3 -> Color((p * 255F + 0.5F).toInt(), (q * 255F + 0.5F).toInt(), (value * 255F + 0.5F).toInt())
                4 -> Color((t * 255F + 0.5F).toInt(), (p * 255F + 0.5F).toInt(), (value * 255F + 0.5F).toInt())
                5 -> Color((value * 255F + 0.5F).toInt(), (p * 255F + 0.5F).toInt(), (q * 255F + 0.5F).toInt())
                else -> Color(0, 0, 0)
            }
        }

        /**
         * Creates a new colour from the given [hsv] colour.
         *
         * @param hsv the hsv colour
         * @return the new colour
         */
        @JvmStatic
        public fun fromHsv(hsv: HSVLike): Color = fromHsv(hsv.h(), hsv.s(), hsv.v())
    }
}
