/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.builder

import net.kyori.adventure.util.HSVLike
import net.kyori.adventure.util.RGBLike
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.effect.particle.ParticleDsl
import java.awt.Color

/**
 * The base builder for building colour particle effects.
 */
public interface BaseColorParticleEffectBuilder<B : BaseColorParticleEffectBuilder<B>> : BaseParticleEffectBuilder<B> {

    /**
     * Sets the color of the particle to the given [red], [green], and [blue]
     * values.
     *
     * Note: If any of the given [red], [green], or [blue] values have more
     * than 8 bits, any excess bits will be "cut off", resulting in a value
     * that is 8 bits. This cut off can be calculated by `value & 0xFF`.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    public fun rgb(red: Int, green: Int, blue: Int): B

    /**
     * Sets the color of the particle to the given [color].
     *
     * @param color the color
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun color(color: Color): B = rgb(color.red, color.green, color.blue)

    /**
     * Sets the color of the particle to the given [rgb] value.
     *
     * Note: If any of the decoded RGB values have more than 8 bits, any excess
     * bits will be "cut off", resulting in a value that is 8 bits. This cut
     * off can be calculated by `value & 0xFF`.
     *
     * @param rgb the RGB value
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    public fun rgb(rgb: Int): B = rgb(rgb shr 16 and 0xFF, rgb shr 8 and 0xFF, rgb and 0xFF)

    /**
     * Sets the color of the particle to the given [rgb] like object.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB like object
     * @return this builder
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun rgb(rgb: RGBLike): B = rgb(rgb.red(), rgb.green(), rgb.blue())

    /**
     * Sets the color of the particle to the given [hue], [saturation], and
     * [value].
     *
     * Note: The [hue], [saturation], and [value] given here must be between
     * 0 and 1, due to them being HSB values.
     *
     * @param hue the hue
     * @param saturation the saturation
     * @param value the value
     * @return this builder
     * @throws IllegalArgumentException if any of the values are not between 0
     * and 1
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun hsv(hue: Float, saturation: Float, value: Float): B {
        require(hue in 0F..1F) { "Hue must be between 0 and 1!" }
        require(saturation in 0F..1F) { "Saturation must be between 0 and 1!" }
        require(value in 0F..1F) { "Value must be between 0 and 1!" }
        return rgb(Color.HSBtoRGB(hue, saturation, value))
    }

    /**
     * Sets the color of the particle to the given [hsv] like object.
     *
     * Note: The values given here in the [hsv] like object must be between
     * 0 and 1, due to them being HSB values.
     *
     * @param hsv the HSV value
     * @return this builder
     * @throws IllegalArgumentException if any of the values are not between 0
     * and 1
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun hsv(hsv: HSVLike): B = hsv(hsv.h(), hsv.s(), hsv.v())
}
