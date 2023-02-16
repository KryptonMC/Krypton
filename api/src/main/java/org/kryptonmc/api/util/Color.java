/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util;

import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;

/**
 * A standard colour in RGB format, with a red, green, and blue component.
 *
 * @param red the red component
 * @param green the green component
 * @param blue the blue component
 */
public record Color(int red, int green, int blue) implements RGBLike {

    public static final @NotNull Color BLACK = new Color(0, 0, 0);
    public static final @NotNull Color WHITE = new Color(255, 255, 255);
    public static final @NotNull Color RED = new Color(255, 0, 0);
    public static final @NotNull Color GREEN = new Color(0, 255, 0);
    public static final @NotNull Color BLUE = new Color(0, 0, 255);
    public static final @NotNull Color YELLOW = new Color(255, 255, 0);
    public static final @NotNull Color MAGENTA = new Color(255, 0, 255);
    public static final @NotNull Color CYAN = new Color(0, 255, 255);
    public static final @NotNull Color GRAY = new Color(128, 128, 128);

    /**
     * Creates a new colour from the given hue, saturation, and value
     * components.
     *
     * @param hue the hue, between 0 and 1
     * @param saturation the saturation, between 0 and 1
     * @param value the value, between 0 and 1
     * @return the new colour
     */
    public static @NotNull Color fromHsv(final float hue, final float saturation, final float value) {
        // This is taken from java.awt.Color#HSBtoRGB
        if (saturation == 0F) {
            final int result = (int) (value * 255F + 0.5F);
            return new Color(result, result, result);
        }

        final float h = (hue - (float) Math.floor(hue)) * 6F;
        final float f = h - (float) Math.floor(h);
        final float p = value * (1F - saturation);
        final float q = value * (1F - saturation * f);
        final float t = value * (1F - (saturation * (1F - f)));

        return switch ((int) h) {
            case 0 -> new Color((int) (value * 255F + 0.5F), (int) (t * 255F + 0.5F), (int) (p * 255F + 0.5F));
            case 1 -> new Color((int) (q * 255F + 0.5F), (int) (value * 255F + 0.5F), (int) (p * 255F + 0.5F));
            case 2 -> new Color((int) (p * 255F + 0.5F), (int) (value * 255F + 0.5F), (int) (t * 255F + 0.5F));
            case 3 -> new Color((int) (p * 255F + 0.5F), (int) (q * 255F + 0.5F), (int) (value * 255F + 0.5F));
            case 4 -> new Color((int) (t * 255F + 0.5F), (int) (p * 255F + 0.5F), (int) (value * 255F + 0.5F));
            case 5 -> new Color((int) (value * 255F + 0.5F), (int) (p * 255F + 0.5F), (int) (q * 255F + 0.5F));
            default -> BLACK;
        };
    }

    /**
     * Creates a new colour from the given hsv colour.
     *
     * @param hsv the hsv colour
     * @return the new colour
     */
    public static @NotNull Color fromHsv(final @NotNull HSVLike hsv) {
        return fromHsv(hsv.h(), hsv.s(), hsv.v());
    }

    public Color {
        if (red < 0 || red > 255) throw new IllegalArgumentException("Red component must be between 0 and 255!");
        if (green < 0 || green > 255) throw new IllegalArgumentException("Green component must be between 0 and 255!");
        if (blue < 0 || blue > 255) throw new IllegalArgumentException("Blue component must be between 0 and 255!");
    }

    /**
     * Creates a new colour from the given encoded RGB value.
     *
     * @param value the encoded RGB value
     */
    public Color(final int value) {
        this((value >> 16) & 0xFF, (value >> 8) & 0xFF, value & 0xFF);
    }

    /**
     * Gets this colour in encoded RGB form.
     *
     * <p>
     * The encoded form of an RGB colour is defined as follows:
     * - Bits 0-7 are the blue component
     * - Bits 8-15 are the green component
     * - Bits 16-23 are the red component
     * - Bits 24-31 are unused
     * </p>
     *
     * @return the encoded RGB value
     */
    public int encode() {
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Creates a new colour with the given new [red] value.
     *
     * @param red the new red value
     * @return the new colour
     */
    public @NotNull Color withRed(final int red) {
        return new Color(red, this.green, this.blue);
    }

    /**
     * Creates a new colour with the given new [green] value.
     *
     * @param green the new green value
     * @return the new colour
     */
    public @NotNull Color withGreen(final int green) {
        return new Color(this.red, green, this.blue);
    }

    /**
     * Creates a new colour with the given new [blue] value.
     *
     * @param blue the new blue value
     * @return the new colour
     */
    public @NotNull Color withBlue(final int blue) {
        return new Color(this.red, this.green, blue);
    }
}
