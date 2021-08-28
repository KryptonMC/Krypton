/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.util.Buildable
import net.kyori.adventure.util.HSVLike
import net.kyori.adventure.util.RGBLike
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector
import java.awt.Color

/**
 * Allows building a [ParticleEffect] for simple particle effects using method chaining.
 */
open class ParticleEffectBuilder @JvmOverloads constructor(
    protected val type: ParticleType,
    protected var quantity: Int = 1,
    protected var offset: Vector = Vector.ZERO,
    protected var longDistance: Boolean = false
) : Buildable.Builder<ParticleEffect> {

    /**
     * Sets the number of particles to be spawned by the [ParticleEffect].
     *
     * @param quantity the number of particles, must be between 1 and 16384 inclusively
     */
    @Contract("_ -> this", mutates = "this")
    open fun quantity(quantity: Int) = apply { this.quantity = quantity }

    /**
     * Sets the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     */
    @Contract("_ -> this", mutates = "this")
    open fun offset(offset: Vector) = apply { this.offset = offset }

    /**
     * Sets if particles can be viewed from a further distance than normal.
     *
     * When false, the view distance is 256.
     * When true, the view distance is 65536.
     *
     * @param longDistance true for long view distance, false for normal view distance
     */
    @Contract("_ -> this", mutates = "this")
    open fun longDistance(longDistance: Boolean) = apply { this.longDistance = longDistance }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance)
}

/**
 * Allows building a [ParticleEffect] for directional particle effects using method chaining.
 */
class DirectionalParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var direction: Vector? = null,
    private var velocity: Float = 0.0F
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the direction of the particles.
     *
     * @param direction the direction of the particles
     */
    @Contract("_ -> this", mutates = "this")
    fun direction(direction: Vector) = apply { this.direction = direction }

    /**
     * Sets the velocity of the particles.
     *
     * The actual velocity tends to vary largely for each particle type, so it's
     * quite arbitrary what this means.
     *
     * @param velocity the velocity of the particles
     */
    @Contract("_ -> this", mutates = "this")
    fun velocity(velocity: Float) = apply { this.velocity = velocity }

    override fun quantity(quantity: Int) = super.quantity(quantity) as DirectionalParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as DirectionalParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as DirectionalParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, DirectionalParticleData(direction, velocity))
}

/**
 * Allows building a [ParticleEffect] for item particle effects using method chaining.
 */
class ItemParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var item: ItemType = ItemTypes.AIR
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the item data of the texture to be used.
     *
     * @param item the item type to use
     */
    @Contract("_ -> this", mutates = "this")
    fun item(item: ItemType) = apply { this.item = item }

    override fun quantity(quantity: Int) = super.quantity(quantity) as ItemParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as ItemParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as ItemParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, ItemParticleData(item))
}

/**
 * Allows building a [ParticleEffect] for block particle effects using method chaining.
 */
class BlockParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var block: Block = Blocks.STONE
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the block state of the texture to be used.
     *
     * @param block the block
     */
    @Contract("_ -> this", mutates = "this")
    fun block(block: Block) = apply { this.block = block }

    override fun quantity(quantity: Int) = super.quantity(quantity) as BlockParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as BlockParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as BlockParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, BlockParticleData(block))
}

/**
 * Allows building a [ParticleEffect] for colored particle effects using method chaining.
 */
open class ColorParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    protected var red: Short = 0,
    protected var green: Short = 0,
    protected var blue: Short = 0
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the color of the particle to the given [color].
     *
     * @param color the color
     */
    @Contract("_ -> this", mutates = "this")
    open fun color(color: Color) = apply {
        this.red = color.red.toShort()
        this.green = color.green.toShort()
        this.blue = color.blue.toShort()
    }

    /**
     * Sets the color of the particle to the given [red], [green], and [blue] values.
     *
     * Note: if any of the given [red], [green], or [blue] values are > 255, they will
     * become 255.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    open fun rgb(red: Int, green: Int, blue: Int) = apply {
        this.red = (red and 0xFF).toShort()
        this.green = (green and 0xFF).toShort()
        this.blue = (blue and 0xFF).toShort()
    }

    /**
     * Sets the color of the particle to the given [rgb] value.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB value
     */
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    open fun rgb(rgb: Int) = rgb(rgb shr 16, rgb shr 8, rgb)

    /**
     * Sets the color of the particle to the given [rgb] like object.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB like object
     */
    @Contract("_ -> this", mutates = "this")
    open fun rgb(rgb: RGBLike) = rgb(rgb.red(), rgb.green(), rgb.blue())

    /**
     * Sets the color of the particle to the given [hue], [saturation], and
     * [value].
     *
     * Note: The [hue], [saturation], and [value] given here must be between 0 and 1,
     * due to them being HSB values.
     *
     * @param hue the hue
     * @param saturation the saturation
     * @param value the value
     */
    @Contract("_ -> this", mutates = "this")
    open fun hsv(hue: Float, saturation: Float, value: Float): ColorParticleEffectBuilder {
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
    open fun hsv(hsv: HSVLike) = hsv(hsv.h(), hsv.s(), hsv.v())

    override fun quantity(quantity: Int) = super.quantity(quantity) as ColorParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as ColorParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as ColorParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, ColorParticleData(red, green, blue))
}

/**
 * Allows building a [ParticleEffect] for dust particle effects using method chaining.
 */
open class DustParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    red: Short = 255,
    green: Short = 0,
    blue: Short = 0,
    protected var scale: Float = 0F
) : ColorParticleEffectBuilder(type, quantity, offset, longDistance, red, green, blue) {

    /**
     * Sets the scale of the dust particles.
     * Clamped between 0.1 and 4.0.
     *
     * @param scale the scale of the particles
     */
    @Contract("_ -> this", mutates = "this")
    open fun scale(scale: Float) = apply { this.scale = scale }

    override fun quantity(quantity: Int) = super.quantity(quantity) as DustParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as DustParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as DustParticleEffectBuilder

    override fun color(color: Color) = super.color(color) as DustParticleEffectBuilder

    override fun rgb(red: Int, green: Int, blue: Int) = super.rgb(red, green, blue) as DustParticleEffectBuilder

    override fun rgb(rgb: Int) = super.rgb(rgb) as DustParticleEffectBuilder

    override fun rgb(rgb: RGBLike) = super.rgb(rgb) as DustParticleEffectBuilder

    override fun hsv(hue: Float, saturation: Float, value: Float) = super.hsv(hue, saturation, value) as DustParticleEffectBuilder

    override fun hsv(hsv: HSVLike) = super.hsv(hsv) as DustParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, DustParticleData(ColorParticleData(red, green, blue), scale))
}

/**
 * Allows building a [ParticleEffect] for dust color transition particle effects using method chaining.
 */
class DustTransitionParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    fromRed: Short = 0,
    fromGreen: Short = 0,
    fromBlue: Short = 0,
    scale: Float = 0F,
    private var toRed: Short = 0,
    private var toGreen: Short = 0,
    private var toBlue: Short = 0
) : DustParticleEffectBuilder(type, quantity, offset, longDistance, fromRed, fromGreen, fromBlue, scale) {

    /**
     * Sets the color to transition the particle to to the given [color].
     *
     * @param color the color
     */
    @Contract("_ -> this", mutates = "this")
    fun toColor(color: Color) = toRGB(color.red, color.green, color.blue)

    /**
     * Sets the color to transition the particle to to the given [red], [green], and
     * [blue] values.
     *
     * Note: if any of the given [red], [green], or [blue] values are > 255, they will
     * become 255.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    @Contract("_ -> this", mutates = "this")
    @Suppress("MagicNumber")
    fun toRGB(red: Int, green: Int, blue: Int) = apply {
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
    fun toRGB(rgb: Int) = rgb(rgb shr 16, rgb shr 8, rgb)

    /**
     * Sets the color to transition the particle to to the given [rgb] like object.
     *
     * Note: if any of the decoded RGB values are > 255, they will become 255.
     *
     * @param rgb the RGB like object
     */
    @Contract("_ -> this", mutates = "this")
    fun toRGB(rgb: RGBLike) = rgb(rgb.red(), rgb.green(), rgb.blue())

    /**
     * Sets the color to transition the particle to to the given [hsv] like object.
     *
     * Note: if any of the decoded HSV values are > 1, they will become 1.
     *
     * @param hsv the HSV value
     */
    @Contract("_ -> this", mutates = "this")
    fun toHSV(hsv: HSVLike) = hsv(hsv.h(), hsv.s(), hsv.v())

    /**
     * Sets the color to transition the particle to to the given [hue], [saturation], and
     * [value].
     *
     * Note: The [hue], [saturation], and [value] given here must be between 0 and 1,
     * due to them being HSB values.
     *
     * @param hue the hue
     * @param saturation the saturation
     * @param value the value
     */
    @Contract("_ -> this", mutates = "this")
    fun toHSV(hue: Float, saturation: Float, value: Float): ColorParticleEffectBuilder {
        require(hue in 0F..1F) { "Hue must be between 0 and 1!" }
        require(saturation in 0F..1F) { "Saturation must be between 0 and 1!" }
        require(value in 0F..1F) { "Value must be between 0 and 1!" }
        return rgb(Color.HSBtoRGB(hue, saturation, value))
    }

    override fun quantity(quantity: Int) = super.quantity(quantity) as DustTransitionParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as DustTransitionParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as DustTransitionParticleEffectBuilder

    override fun color(color: Color) = super.color(color) as DustTransitionParticleEffectBuilder

    override fun rgb(red: Int, green: Int, blue: Int) = super.rgb(red, green, blue) as DustTransitionParticleEffectBuilder

    override fun rgb(rgb: Int) = super.rgb(rgb) as DustTransitionParticleEffectBuilder

    override fun rgb(rgb: RGBLike) = super.rgb(rgb) as DustTransitionParticleEffectBuilder

    override fun hsv(hue: Float, saturation: Float, value: Float) = super.hsv(hue, saturation, value) as DustTransitionParticleEffectBuilder

    override fun hsv(hsv: HSVLike) = super.hsv(hsv) as DustTransitionParticleEffectBuilder

    override fun scale(scale: Float) = super.scale(scale) as DustTransitionParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(
        type,
        quantity,
        offset,
        longDistance,
        DustTransitionParticleData(ColorParticleData(red, green, blue), scale, ColorParticleData(toRed, toGreen, toBlue))
    )
}

/**
 * Allows building a [ParticleEffect] for note particle effects using method chaining.
 */
class NoteParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var note: Byte = 0
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the type of note to use.
     *
     * Must be between 0 and 24 inclusively.
     *
     * @param note the note value
     */
    @Contract("_ -> this", mutates = "this")
    fun note(note: Int) = apply {
        require(note in 0..24) { "Note must be between 0 and 24!" }
        this.note = note.toByte()
    }

    override fun quantity(quantity: Int) = super.quantity(quantity) as NoteParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as NoteParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as NoteParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, NoteParticleData(note))
}

/**
 * Allows building a [ParticleEffect] for vibration particle effects using method chaining.
 */
class VibrationParticleEffectBuilder @JvmOverloads constructor(
    type: ParticleType,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var origin: Position = Vector.ZERO,
    private var destination: Position = Vector.ZERO,
    private var ticks: Int = 0
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the origin location from the given [position].
     *
     * @param position the origin position
     */
    @Contract("_ -> this", mutates = "this")
    fun origin(position: Position) = apply { origin = position }

    /**
     * Sets the destination location from the given [position].
     *
     * @param position the destination position
     */
    @Contract("_ -> this", mutates = "this")
    fun destination(position: Position) = apply { origin = position }

    /**
     * Sets the amount of ticks it will take to vibrate from the
     * origin to the destination.
     *
     * @param ticks the amount of ticks
     */
    @Contract("_ -> this", mutates = "this")
    fun ticks(ticks: Int) = apply { this.ticks = ticks }

    override fun quantity(quantity: Int) = super.quantity(quantity) as VibrationParticleEffectBuilder

    override fun offset(offset: Vector) = super.offset(offset) as VibrationParticleEffectBuilder

    override fun longDistance(longDistance: Boolean) = super.longDistance(longDistance) as VibrationParticleEffectBuilder

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, VibrationParticleData(origin, destination, ticks))
}
