/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmName("ParticleFactory")
package org.kryptonmc.api.effect.particle

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector

/**
 * Allows building a [ParticleEffect] for simple particle effects using method chaining.
 */
open class ParticleEffectBuilder internal constructor(
    protected val type: Particle,
    protected var quantity: Int = 1,
    protected var offset: Vector = Vector.ZERO,
    protected var longDistance: Boolean = false
) {

    /**
     * Set the number of particles to be spawned by the [ParticleEffect].
     *
     * @param quantity the number of particles, must be between 1 and 16384 inclusively
     */
    @Contract("_ -> this")
    fun quantity(quantity: Int) = apply { this.quantity = quantity }

    /**
     * Set the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     */
    @Contract("_ -> this")
    fun offset(offset: Vector) = apply { this.offset = offset }

    /**
     * Set if particles can be viewed from a further distance than normal.
     *
     * When false, the view distance is 256.
     * When true, the view distance is 65536.
     *
     * @param longDistance true for long view distance, false for normal view distance
     */
    @Contract("_ -> this")
    fun longDistance(longDistance: Boolean) = apply { this.longDistance = longDistance }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new", pure = true)
    @JvmSynthetic
    internal open fun build() = ParticleEffect(type, quantity, offset, longDistance)
}

/**
 * Allows building a [ParticleEffect] for directional particle effects using method chaining.
 */
class DirectionalParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var direction: Vector? = null,
    private var velocity: Float = 0.0F
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the direction of the particles.
     *
     * @param direction the direction of the particles
     */
    @Contract("_ -> this")
    fun direction(direction: Vector) = apply { this.direction = direction }

    /**
     * Set the velocity of the particles.
     * The actual velocity tends to vary largely for each particle type, so it's quite arbitrary what this means.
     *
     * @param velocity the velocity of the particles
     */
    @Contract("_ -> this")
    fun velocity(velocity: Float) = apply { this.velocity = velocity }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new", pure = true)
    @JvmSynthetic
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, DirectionalParticleData(direction, velocity))
}

/**
 * Allows building a [ParticleEffect] for item particle effects using method chaining.
 */
class ItemParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var itemId: Int = 1 // TODO: Item
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the item data of the texture to be used.
     *
     * @param itemId the item ID to use
     */
    @Contract("_ -> this")
    fun item(itemId: Int) = apply { this.itemId = itemId }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new", pure = true)
    @JvmSynthetic
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, ItemParticleData(itemId))
}

/**
 * Allows building a [ParticleEffect] for block particle effects using method chaining.
 */
class BlockParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var blockId: Int = 1 // TODO: Block
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the block state of the texture to be used.
     *
     * @param blockId the block state ID to use
     */
    @Contract("_ -> this")
    fun block(blockId: Int) = apply { this.blockId = blockId }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new", pure = true)
    @JvmSynthetic
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, BlockParticleData(blockId))
}

/**
 * Allows building a [ParticleEffect] for colored particle effects using method chaining.
 */
open class ColorParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    protected var red: UByte = 0u,
    protected var green: UByte = 0u,
    protected var blue: UByte = 0u
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the color of the particle.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    @Contract("_ -> this")
    fun color(red: UByte, green: UByte, blue: UByte) = apply {
        this.red = red
        this.green = green
        this.blue = blue
    }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new")
    @JvmSynthetic
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, ColorParticleData(red, green, blue))
}

/**
 * Allows building a [ParticleEffect] for dust particle effects using method chaining.
 */
open class DustParticleEffectBuilder internal constructor(
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
     * Set the scale of the dust particles.
     * Clamped between 0.1 and 4.0.
     *
     * @param scale the scale of the particles
     */
    @Contract("_ -> this")
    fun scale(scale: Float) = apply { this.scale = scale }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new")
    @JvmSynthetic
    override fun build() =
        ParticleEffect(type, quantity, offset, longDistance, DustParticleData(ColorParticleData(red, green, blue), scale))
}

/**
 * Allows building a [ParticleEffect] for dust color transitionparticle effects using method chaining.
 */
class DustTransitionParticleEffectBuilder internal constructor(
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
    @Contract("_ -> this")
    fun toColor(red: UByte, green: UByte, blue: UByte) = apply {
        toRed = red
        toGreen = green
        toBlue = blue
    }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
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
class NoteParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var note: UByte = 0u
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the type of note to use.
     * Must be between 0 and 24 inclusively.
     *
     * @param note the note value
     */
    @Contract("_ -> this")
    fun note(note: UByte) = apply { this.note = note }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    @Contract("_ -> new")
    @JvmSynthetic
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, data = NoteParticleData(note))
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
    @Contract("_ -> this")
    fun origin(position: Position) = apply { origin = position }

    /**
     * Sets the destination location from the given [position].
     *
     * @param position the destination position
     */
    @Contract("_ -> this")
    fun destination(position: Position) = apply { origin = position }

    /**
     * Sets the amount of ticks it will take to vibrate from the
     * origin to the destination.
     *
     * @param ticks the amount of ticks
     */
    @Contract("_ -> this")
    fun ticks(ticks: Int) = apply { this.ticks = ticks }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() =
        ParticleEffect(type, quantity, offset, longDistance, VibrationParticleData(origin, destination, ticks))
}

/**
 * DSL to create simple [ParticleEffect]s.
 *
 * @param particleType type of simple particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [ParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: SimpleParticle, lambda: ParticleEffectBuilder.() -> Unit = {}) =
    ParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create directional [ParticleEffect]s.
 *
 * @param particleType type of directional particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [DirectionalParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: DirectionalParticle, lambda: DirectionalParticleEffectBuilder.() -> Unit = {}) =
    DirectionalParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create block [ParticleEffect]s.
 *
 * @param particleType type of block particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [BlockParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: BlockParticle, lambda: BlockParticleEffectBuilder.() -> Unit = {}) =
    BlockParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create item [ParticleEffect]s.
 *
 * @param particleType type of particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using an [ItemParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: ItemParticle, lambda: ItemParticleEffectBuilder.() -> Unit = {}) =
    ItemParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create colored [ParticleEffect]s.
 *
 * @param particleType type of particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [ColorParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: ColorParticle, lambda: ColorParticleEffectBuilder.() -> Unit = {}) =
    ColorParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create dust [ParticleEffect]s.
 *
 * @param particleType type of particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [DustParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: DustParticle, lambda: DustParticleEffectBuilder.() -> Unit = {}) =
    DustParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create dust transition [ParticleEffect]s.
 *
 * @param particleType the type of particle in [ParticleType]
 * @param builder function to allow configuring the [ParticleEffect] using a [DustTransitionParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: DustTransitionParticle, builder: DustTransitionParticleEffectBuilder.() -> Unit = {}) =
    DustTransitionParticleEffectBuilder(particleType).apply(builder).build()

/**
 * DSL to create note [ParticleEffect]s.
 *
 * @param particleType type of particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [NoteParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: NoteParticle, lambda: NoteParticleEffectBuilder.() -> Unit = {}) =
    NoteParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create vibration [ParticleEffect]s.
 *
 * @param particleType the type of particle in [ParticleType]
 * @param builder function to allow configuring the [ParticleEffect] using a [VibrationParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 */
@JvmName("of")
fun particleEffect(particleType: VibrationParticle, builder: VibrationParticleEffectBuilder.() -> Unit = {}) =
    VibrationParticleEffectBuilder(particleType).apply(builder).build()
