package org.kryptonmc.krypton.api.block

import org.kryptonmc.krypton.api.space.Vector

/**
 * Represents a bounding box. This defines the box for any entity or block that may be collided with.
 *
 * @author Callum Seabrook
 */
data class BoundingBox(
    val minimum: Vector,
    val maximum: Vector
) : Cloneable {

    constructor() : this(Vector.ZERO, Vector.ZERO)

    val size by lazy { Vector(maximum.x - minimum.x, maximum.y - minimum.y, maximum.z - minimum.z) }

    val volume by lazy { size.x * size.y * size.z }

    val center by lazy { Vector(minimum.x + size.x * 0.5, minimum.y + size.y * 0.5, minimum.z + size.z * 0.5) }
}