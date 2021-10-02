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
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.DustTransitionParticleData
import org.spongepowered.math.vector.Vector3d
import java.awt.Color

/**
 * Allows building a [ParticleEffect] for dust color transition particle
 * effects using method chaining.
 */
public class DustTransitionParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    fromRed: Short = 0,
    fromGreen: Short = 0,
    fromBlue: Short = 0,
    scale: Float = 0F,
    private var toRed: Short = 0,
    private var toGreen: Short = 0,
    private var toBlue: Short = 0
) : AbstractDustParticleEffectBuilder<DustTransitionParticleEffectBuilder>(
    type,
    quantity,
    offset,
    longDistance,
    fromRed,
    fromGreen,
    fromBlue,
    scale
) {

    /**
     * Sets the color to transition the particle to to the given [color].
     *
     * @param color the color
     */
    @Contract("_ -> this", mutates = "this")
    public fun toColor(color: Color): DustTransitionParticleEffectBuilder = toRGB(color.red, color.green, color.blue)

    /**
     * Sets the color to transition the particle to to the given [red],
     * [green], and [blue] values.
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
    public fun toRGB(red: Int, green: Int, blue: Int): DustTransitionParticleEffectBuilder = apply {
        toRed = (red and 0xFF).toShort()
        toGreen = (green and 0xFF).toShort()
        toBlue = (blue and 0xFF).toShort()
    }

    /**
     * Sets the color to transition the particle to to the given [rgb] value.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB value
     */
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    public fun toRGB(rgb: Int): DustTransitionParticleEffectBuilder = rgb(rgb shr 16, rgb shr 8, rgb)

    /**
     * Sets the color to transition the particle to to the given [rgb] like
     * object.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB like object
     */
    @Contract("_ -> this", mutates = "this")
    public fun toRGB(rgb: RGBLike): DustTransitionParticleEffectBuilder = rgb(rgb.red(), rgb.green(), rgb.blue())

    /**
     * Sets the color to transition the particle to to the given [hsv] like
     * object.
     *
     * Note: if any of the decoded HSV values are > 1, they will become 1.
     *
     * @param hsv the HSV value
     */
    @Contract("_ -> this", mutates = "this")
    public fun toHSV(hsv: HSVLike): DustTransitionParticleEffectBuilder = hsv(hsv.h(), hsv.s(), hsv.v())

    /**
     * Sets the color to transition the particle to to the given [hue],
     * [saturation], and [value].
     *
     * Note: The [hue], [saturation], and [value] given here must be between
     * 0 and 1, due to them being HSB values.
     *
     * @param hue the hue
     * @param saturation the saturation
     * @param value the value
     */
    @Contract("_ -> this", mutates = "this")
    public fun toHSV(hue: Float, saturation: Float, value: Float): DustTransitionParticleEffectBuilder {
        require(hue in 0F..1F) { "Hue must be between 0 and 1!" }
        require(saturation in 0F..1F) { "Saturation must be between 0 and 1!" }
        require(value in 0F..1F) { "Value must be between 0 and 1!" }
        return rgb(Color.HSBtoRGB(hue, saturation, value))
    }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build(): ParticleEffect = ParticleEffect.of(
        type,
        quantity,
        offset,
        longDistance,
        DustTransitionParticleData.of(red, green, blue, scale, toRed, toGreen, toBlue)
    )
}
