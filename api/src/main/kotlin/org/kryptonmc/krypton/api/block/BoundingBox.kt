package org.kryptonmc.krypton.api.block

import org.kryptonmc.krypton.api.space.Vector

/**
 * Represents a bounding box. This defines the box for any entity or block that may be collided with.
 */
@Suppress("unused")
data class BoundingBox(
    val minimum: Vector,
    val maximum: Vector
) : Cloneable {

    /**
     * The size of this bounding box
     *
     * May create a new [Vector] instance if this is the first call
     */
    val size by lazy { Vector(maximum.x - minimum.x, maximum.y - minimum.y, maximum.z - minimum.z) }

    /**
     * The volume of this bounding box
     */
    val volume = size.x * size.y * size.z

    /**
     * The centre point of this bounding box
     *
     * May create a new [Vector] instance if this is the first call
     */
    val center by lazy { Vector(minimum.x + size.x * 0.5, minimum.y + size.y * 0.5, minimum.z + size.z * 0.5) }

    /**
     * Create an empty bounding box
     */
    constructor() : this(Vector.ZERO, Vector.ZERO)

    companion object {

        /**
         * Constant for an empty bounding box
         */
        @JvmField
        val EMPTY = BoundingBox()
    }
}
