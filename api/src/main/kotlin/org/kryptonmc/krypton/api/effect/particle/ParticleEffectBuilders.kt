package org.kryptonmc.krypton.api.effect.particle

import org.kryptonmc.krypton.api.space.Vector

/**
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

open class DirectionalParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    private var direction: Vector? = null,
    private var velocity: Float = 0.0F
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the direction of the particles.
     * This value is normalized by the client.
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

open class ItemParticleEffectBuilder internal constructor(
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

open class BlockParticleEffectBuilder internal constructor(
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

open class ColorParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    protected var r: UByte = 0u,
    protected var g: UByte = 0u,
    protected var b: UByte = 0u
) : ParticleEffectBuilder(type, quantity, offset, longDistance) {

    /**
     * Set the color of the particle.
     *
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     */
    fun color(r: UByte, g: UByte, b: UByte) = apply {
        this.r = r
        this.g = g
        this.b = b
    }

    /**
     * @return a new [ParticleEffect] created from this builder
     */
    override fun build() = ParticleEffect(type, quantity, offset, longDistance, data = ColorParticleData(r, g, b))
}

open class DustParticleEffectBuilder internal constructor(
    type: Particle,
    quantity: Int = 1,
    offset: Vector = Vector.ZERO,
    longDistance: Boolean = false,
    r: UByte = 255u,
    g: UByte = 0u,
    b: UByte = 0u,
    private var scale: Float = 0.0F
) : ColorParticleEffectBuilder(type, quantity, offset, longDistance, r, g, b) {

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
        ParticleEffect(type, quantity, offset, longDistance, data = DustParticleData(ColorParticleData(r, g, b), scale))
}

open class NoteParticleEffectBuilder internal constructor(
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

fun particleEffect(particleType: SimpleParticle, lambda: ParticleEffectBuilder.() -> Unit) =
    ParticleEffectBuilder(particleType).apply(lambda).build()

fun particleEffect(particleType: DirectionalParticle, lambda: DirectionalParticleEffectBuilder.() -> Unit) =
    DirectionalParticleEffectBuilder(particleType).apply(lambda).build()

fun particleEffect(particleType: BlockParticle, lambda: BlockParticleEffectBuilder.() -> Unit) =
    BlockParticleEffectBuilder(particleType).apply(lambda).build()

fun particleEffect(particleType: ItemParticle, lambda: ItemParticleEffectBuilder.() -> Unit) =
    ItemParticleEffectBuilder(particleType).apply(lambda).build()

fun particleEffect(particleType: ColorParticle, lambda: ColorParticleEffectBuilder.() -> Unit) =
    ColorParticleEffectBuilder(particleType).apply(lambda).build()

fun particleEffect(particleType: DustParticle, lambda: DustParticleEffectBuilder.() -> Unit) =
    DustParticleEffectBuilder(particleType).apply(lambda).build()

fun particleEffect(particleType: NoteParticle, lambda: NoteParticleEffectBuilder.() -> Unit) =
    NoteParticleEffectBuilder(particleType).apply(lambda).build()
