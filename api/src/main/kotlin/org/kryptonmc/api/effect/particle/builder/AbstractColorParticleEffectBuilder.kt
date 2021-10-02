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
import org.kryptonmc.api.effect.particle.ParticleType
import org.spongepowered.math.vector.Vector3d
import java.awt.Color

/**
 * The base class for all color particle effect builders. Used to abstract away
 * messy recursive builder logic.
 */
@Suppress("UNCHECKED_CAST")
public sealed class AbstractColorParticleEffectBuilder<B : AbstractColorParticleEffectBuilder<B>>(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    protected var red: Short = 0,
    protected var green: Short = 0,
    protected var blue: Short = 0
) : AbstractParticleEffectBuilder<B>(type, quantity, offset, longDistance) {

    /**
     * Sets the color of the particle to the given [color].
     *
     * @param color the color
     */
    @Contract("_ -> this", mutates = "this")
    public fun color(color: Color): B = apply {
        this.red = color.red.toShort()
        this.green = color.green.toShort()
        this.blue = color.blue.toShort()
    } as B

    /**
     * Sets the color of the particle to the given [red], [green], and [blue]
     * values.
     *
     * Note: if any of the given [red], [green], or [blue] values are > 255,
     * they will become 255.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    public fun rgb(red: Int, green: Int, blue: Int): B = apply {
        this.red = (red and 0xFF).toShort()
        this.green = (green and 0xFF).toShort()
        this.blue = (blue and 0xFF).toShort()
    } as B

    /**
     * Sets the color of the particle to the given [rgb] value.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB value
     */
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    public fun rgb(rgb: Int): B = rgb(rgb shr 16, rgb shr 8, rgb)

    /**
     * Sets the color of the particle to the given [rgb] like object.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB like object
     */
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
     */
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
     * Note: if any of the decoded HSV values are > 1, they will become 1.
     *
     * @param hsv the HSV value
     */
    @Contract("_ -> this", mutates = "this")
    public fun hsv(hsv: HSVLike): B = hsv(hsv.h(), hsv.s(), hsv.v())
}
