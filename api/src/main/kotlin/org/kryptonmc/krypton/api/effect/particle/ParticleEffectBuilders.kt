@file:JvmName("ParticleUtils")
package org.kryptonmc.krypton.api.effect.particle

import org.kryptonmc.krypton.api.space.Vector

/**
 * Allows building a [ParticleEffect] for simple particle effects using method chaining.
 *
 * @author Esophose
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
    fun quantity(quantity: Int) = apply { this.quantity = quantity }

    /**
     * Set the offset the particles can be from the origin.
     *
     * @param offset the offset from the origin
     */
    fun offset(offset: Vector) = apply { this.offset = offset }

    /**
     * Set if particles can be viewed from a further distance than normal.
     *
     * When false, the view distance is 256.
     * When true, the view distance is 65536.
     *
     * @param longDistance true for long view distance, false for normal view distance
     */
    fun longDistance(longDistance: Boolean) = apply { this.longDistance = longDistance }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    internal open fun build() = ParticleEffect(type, quantity, offset, longDistance)
}

/**
 * Allows building a [ParticleEffect] for directional particle effects using method chaining.
 *
 * @author Esophose
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
    fun direction(direction: Vector) = apply { this.direction = direction }

    /**
     * Set the velocity of the particles.
     * The actual velocity tends to vary largely for each particle type, so it's quite arbitrary what this means.
     *
     * @param velocity the velocity of the particles
     */
    fun velocity(velocity: Float) = apply { this.velocity = velocity }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, DirectionalParticleData(direction, velocity))
}

/**
 * Allows building a [ParticleEffect] for item particle effects using method chaining.
 *
 * @author Esophose
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
    fun item(itemId: Int) = apply { this.itemId = itemId }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, data = ItemParticleData(itemId))
}

/**
 * Allows building a [ParticleEffect] for block particle effects using method chaining.
 *
 * @author Esophose
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
    fun block(blockId: Int) = apply { this.blockId = blockId }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, data = BlockParticleData(blockId))
}

/**
 * Allows building a [ParticleEffect] for colored particle effects using method chaining.
 *
 * @author Esophose
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
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     */
    fun color(r: UByte, g: UByte, b: UByte) = apply {
        this.red = r
        this.green = g
        this.blue = b
    }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, data = ColorParticleData(red, green, blue))
}

/**
 * Allows building a [ParticleEffect] for dust particle effects using method chaining.
 *
 * @author Esophose
 */
class DustParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    red: UByte = 255u,
    green: UByte = 0u,
    blue: UByte = 0u,
    private var scale: Float = 0.0F
) : ColorParticleEffectBuilder(type, quantity, offset, longDistance, red, green, blue) {

    /**
     * Set the scale of the dust particles.
     * Clamped between 0.1 and 4.0.
     *
     * @param scale the scale of the particles
     */
    fun scale(scale: Float) = apply { this.scale = scale }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() =
        ParticleEffect(type, quantity, offset, longDistance, data = DustParticleData(ColorParticleData(red, green, blue), scale))
}

/**
 * Allows building a [ParticleEffect] for note particle effects using method chaining.
 *
 * @author Esophose
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
    fun note(note: UByte) = apply { this.note = note }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, data = NoteParticleData(note))
}

/**
 * DSL to create simple [ParticleEffect]s.
 *
 * @param particleType type of simple particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [ParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 * @author Esophose
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
 * @author Esophose
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
 * @author Esophose
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
 * @author Esophose
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
 * @author Esophose
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
 * @author Esophose
 */
@JvmName("of")
fun particleEffect(particleType: DustParticle, lambda: DustParticleEffectBuilder.() -> Unit = {}) =
    DustParticleEffectBuilder(particleType).apply(lambda).build()

/**
 * DSL to create note [ParticleEffect]s.
 *
 * @param particleType type of particle in [ParticleType]
 * @param lambda function to allow configuring the [ParticleEffect] using a [NoteParticleEffectBuilder]
 * @return a new [ParticleEffect] based on the given settings
 * @author Esophose
 */
@JvmName("of")
fun particleEffect(particleType: NoteParticle, lambda: NoteParticleEffectBuilder.() -> Unit = {}) =
    NoteParticleEffectBuilder(particleType).apply(lambda).build()