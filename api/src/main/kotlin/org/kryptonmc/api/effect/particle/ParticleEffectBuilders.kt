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
open class ParticleEffectBuilder(
    protected val type: Particle,
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
    fun quantity(quantity: Int) = apply { this.quantity = quantity }

    /**
     * Sets the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     */
    @Contract("_ -> this", mutates = "this")
    fun offset(offset: Vector) = apply { this.offset = offset }

    /**
     * Sets if particles can be viewed from a further distance than normal.
     *
     * When false, the view distance is 256.
     * When true, the view distance is 65536.
     *
     * @param longDistance true for long view distance, false for normal view distance
     */
    @Contract("_ -> this", mutates = "this")
    fun longDistance(longDistance: Boolean) = apply { this.longDistance = longDistance }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance)
}

/**
 * Allows building a [ParticleEffect] for directional particle effects using method chaining.
 */
class DirectionalParticleEffectBuilder(
    type: Particle,
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

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, DirectionalParticleData(direction, velocity))
}

/**
 * Allows building a [ParticleEffect] for item particle effects using method chaining.
 */
class ItemParticleEffectBuilder(
    type: Particle,
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

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, ItemParticleData(item))
}

/**
 * Allows building a [ParticleEffect] for block particle effects using method chaining.
 */
class BlockParticleEffectBuilder(
    type: Particle,
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

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, BlockParticleData(block))
}

/**
 * Allows building a [ParticleEffect] for colored particle effects using method chaining.
 */
open class ColorParticleEffectBuilder(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    protected var red: UByte = 0u,
    protected var green: UByte = 0u,
    protected var blue: UByte = 0u
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the color of the particle.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    @Contract("_ -> this", mutates = "this")
    fun color(red: UByte, green: UByte, blue: UByte) = apply {
        this.red = red
        this.green = green
        this.blue = blue
    }

    /**
     * Sets the color of the particle.
     *
     * @param color the color
     */
    @Contract("_ -> this", mutates = "this")
    fun color(color: Color) = apply {
        this.red = color.red.toUByte()
        this.green = color.green.toUByte()
        this.blue = color.blue.toUByte()
    }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, ColorParticleData(red, green, blue))
}

/**
 * Allows building a [ParticleEffect] for dust particle effects using method chaining.
 */
open class DustParticleEffectBuilder(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    red: UByte = 255u,
    green: UByte = 0u,
    blue: UByte = 0u,
    protected var scale: Float = 0F
) : ColorParticleEffectBuilder(type, quantity, offset, longDistance, red, green, blue) {

    /**
     * Sets the scale of the dust particles.
     * Clamped between 0.1 and 4.0.
     *
     * @param scale the scale of the particles
     */
    @Contract("_ -> this", mutates = "this")
    fun scale(scale: Float) = apply { this.scale = scale }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, DustParticleData(ColorParticleData(red, green, blue), scale))
}

/**
 * Allows building a [ParticleEffect] for dust color transitionparticle effects using method chaining.
 */
class DustTransitionParticleEffectBuilder(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    fromRed: UByte = 0u,
    fromGreen: UByte = 0u,
    fromBlue: UByte = 0u,
    scale: Float = 0F,
    private var toRed: UByte = 0u,
    private var toGreen: UByte = 0u,
    private var toBlue: UByte = 0u
) : DustParticleEffectBuilder(type, quantity, offset, longDistance, fromRed, fromGreen, fromBlue, scale) {

    /**
     * Sets the color to transition to.
     *
     * @param red the red to transition to
     * @param green the green to transition to
     * @param blue the blue to transition to
     */
    @Contract("_ -> this", mutates = "this")
    fun toColor(red: UByte, green: UByte, blue: UByte) = apply {
        toRed = red
        toGreen = green
        toBlue = blue
    }

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
class NoteParticleEffectBuilder(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var note: UByte = 0u
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Sets the type of note to use.
     * Must be between 0 and 24 inclusively.
     *
     * @param note the note value
     */
    @Contract("_ -> this", mutates = "this")
    fun note(note: UByte) = apply { this.note = note }

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, NoteParticleData(note))
}

/**
 * Allows building a [ParticleEffect] for vibration particle effects using method chaining.
 */
class VibrationParticleEffectBuilder(
    type: Particle,
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

    /**
     * Builds a new [ParticleEffect] from the settings of this builder.
     */
    @Contract("_ -> new", pure = true)
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, VibrationParticleData(origin, destination, ticks))
}
