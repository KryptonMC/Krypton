package org.kryptonmc.api.space

import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3i
import kotlin.math.max
import kotlin.math.min

/**
 * A bounding box.
 * These are immutable to guarantee thread-safe usage.
 *
 * @param minimumX the minimum X value
 * @param minimumY the minimum Y value
 * @param minimumZ the minimum Z value
 * @param maximumX the maximum X value
 * @param maximumY the maximum Y value
 * @param maximumZ the maximum Z value
 */
data class BoundingBox(
    val minimumX: Double,
    val minimumY: Double,
    val minimumZ: Double,
    val maximumX: Double,
    val maximumY: Double,
    val maximumZ: Double
) {

    /**
     * The size of this bounding box on the X axis.
     */
    val xSize = maximumX - minimumX

    /**
     * The size of this bounding box on the Y axis.
     */
    val ySize = maximumY - minimumY

    /**
     * The size of this bounding box on the Z axis.
     */
    val zSize = maximumZ - minimumZ

    /**
     * The total size of this bounding box.
     */
    val size = (xSize + ySize + zSize) / 3.0

    /**
     * The total volume of this bounding box.
     */
    val volume = xSize * ySize * zSize

    /**
     * If this box contains a value that is [NaN][Double.isNaN].
     */
    val hasNaN = minimumX.isNaN() || minimumY.isNaN() || minimumZ.isNaN() || maximumX.isNaN() || maximumY.isNaN() || maximumZ.isNaN()

    /**
     * If all values of this box are [NaN][Double.isNaN].
     */
    val isNaN = minimumX.isNaN() && minimumY.isNaN() && minimumZ.isNaN() && maximumX.isNaN() && maximumY.isNaN() && maximumZ.isNaN()

    /**
     * The center of this bounding box.
     */
    val center by lazy { Vector(GenericMath.lerp(0.5, minimumX, maximumX), GenericMath.lerp(0.5, minimumY, maximumY), GenericMath.lerp(0.5, minimumZ, maximumZ)) }

    /**
     * Constructs a new [BoundingBox] from the given [minimum] and [maximum] vectors.
     *
     * @param minimum the minimum vector
     * @param maximum the maximum vector
     */
    constructor(minimum: Vector, maximum: Vector) : this(minimum.x, minimum.y, minimum.z, maximum.x, maximum.y, maximum.z)

    /**
     * Constructs a new [BoundingBox] from the given [minimum] and [maximum] positions.
     *
     * @param minimum the minimum position
     * @param maximum the maximum position
     */
    constructor(minimum: Vector3i, maximum: Vector3i) : this(
        minimum.x().toDouble(),
        minimum.y().toDouble(),
        minimum.z().toDouble(),
        maximum.x().toDouble(),
        maximum.y().toDouble(),
        maximum.z().toDouble()
    )

    /**
     * Constructs a new [BoundingBox] from the given [minimum], with a size of 1,
     * calculating the maximum values by adding 1 to the minimum values.
     *
     * @param minimum the minimum position
     */
    constructor(minimum: Vector3i) : this(
        minimum.x().toDouble(),
        minimum.y().toDouble(),
        minimum.z().toDouble(),
        (minimum.x() + 1).toDouble(),
        (minimum.y() + 1).toDouble(),
        (minimum.z() + 1).toDouble()
    )

    init {
        require(minimumX <= maximumX) { "Minimum X cannot be greater than maximum X!" }
        require(minimumY <= maximumY) { "Minimum Y cannot be greater than maximum Y!" }
        require(minimumZ <= maximumZ) { "Minimum Z cannot be greater than maximum Z!" }
    }

    /**
     * Gets the minimum value for the given [axis].
     *
     * @param axis the axis
     * @return the minimum value for the axis
     */
    fun minimum(axis: Direction.Axis) = axis.select(minimumX, minimumY, minimumZ)

    /**
     * Gets the maximum value for the given [axis].
     *
     * @param axis the axis
     * @return the maximum value for the axis
     */
    fun maximum(axis: Direction.Axis) = axis.select(maximumX, maximumY, maximumZ)

    /**
     * Inflates the border of this bounding box by the given [xFactor], [yFactor], and
     * [zFactor], and returns a new bounding box with the applied changes.
     *
     * @param xFactor the X factor to inflate the border by
     * @param xFactor the Y factor to inflate the border by
     * @param xFactor the Z factor to inflate the border by
     * @return a new bounding box with its border inflated by the given factors
     */
    fun inflate(xFactor: Double, yFactor: Double, zFactor: Double): BoundingBox {
        val newMinX = minimumX - xFactor
        val newMinY = minimumY - yFactor
        val newMinZ = minimumZ - zFactor
        val newMaxX = maximumX + xFactor
        val newMaxY = maximumY + yFactor
        val newMaxZ = maximumZ + zFactor
        return BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Inflates the border of this bounding box by the given [factor], and returns a
     * new bounding box with the applied changes.
     *
     * @param factor the factor to inflate the border by
     * @return a new bounding box with its border inflated by the given factor
     */
    fun inflate(factor: Double) = inflate(factor, factor, factor)

    /**
     * Deflates the border of this bounding box by the given [xFactor], [yFactor], and
     * [zFactor], and returns a new bounding box with the applied changes.
     *
     * This is equivalent to calling `inflate(-xFactor, -yFactor, -zFactor)`
     *
     * @param xFactor the X factor to deflate the border by
     * @param xFactor the Y factor to deflate the border by
     * @param xFactor the Z factor to deflate the border by
     * @return a new bounding box with its border deflated by the given factors
     */
    fun deflate(xFactor: Double, yFactor: Double, zFactor: Double) = inflate(-xFactor, -yFactor, -zFactor)

    /**
     * Deflates the border of this bounding box by the given [factor], and returns a
     * new bounding box with the applied changes.
     *
     * This is equivalent to calling `inflate(-factor)`
     *
     * @param factor the factor to deflate the border by
     * @return a new bounding box with its border deflated by the given factor
     */
    fun deflate(factor: Double) = inflate(-factor)

    /**
     * Intersects this bounding box with the given [other] bounding box, and returns a
     * new bounding box with the resulted intersection.
     *
     * @param other the other box to intersect with
     * @return a new bounding box with the resulting intersection
     */
    fun intersect(other: BoundingBox): BoundingBox {
        val newMinX = max(minimumX, other.minimumX)
        val newMinY = max(minimumY, other.minimumY)
        val newMinZ = max(minimumZ, other.minimumZ)
        val newMaxX = min(maximumX, other.maximumX)
        val newMaxY = min(maximumY, other.maximumY)
        val newMaxZ = min(maximumZ, other.maximumZ)
        return BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Moves this bounding box by the specified [x], [y], and [z] amounts, and returns a
     * new bounding box with the result of the move.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return a new bounding box with the result of the move
     */
    fun move(x: Double, y: Double, z: Double) = BoundingBox(minimumX + x, minimumY + y, minimumZ + z, maximumX + x, maximumY + y, maximumZ + z)

    /**
     * Moves this bounding box by the specified [amount], and returns a new bounding box
     * with the result of the move.
     *
     * @param amount the amount
     * @return a new bounding box with the result of the move
     */
    fun move(amount: Vector) = move(amount.x, amount.y, amount.z)

    /**
     * Expands this bounding box out by the given [x], [y], and [z] amounts, and returns a
     * new bounding box with the result of the expansion.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return a new bounding box with the result of the expansion
     */
    fun expand(x: Double, y: Double, z: Double): BoundingBox {
        var minX = minimumX
        var minY = minimumY
        var minZ = minimumZ
        var maxX = maximumX
        var maxY = maximumY
        var maxZ = maximumZ
        if (x < 0.0) minX += x else if (x > 0.0) maxX += x
        if (y < 0.0) minY += y else if (y > 0.0) maxY += y
        if (z < 0.0) minZ += z else if (z > 0.0) maxZ += z
        return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    /**
     * Expands this bounding box out by the given [amount], and returns a new bounding
     * box with the result of the expansion.
     *
     * @param amount the amount
     * @return a new bounding box with the result of the expansion
     */
    fun expand(amount: Vector) = expand(amount.x, amount.y, amount.z)

    /**
     * Contracts this bounding box by the given [x], [y], and [z] amounts, and returns a
     * new bounding box with the result of the contraction.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @return a new bounding box with the result of the contraction
     */
    fun contract(x: Double, y: Double, z: Double): BoundingBox {
        var minX = minimumX
        var minY = minimumY
        var minZ = minimumZ
        var maxX = maximumX
        var maxY = maximumY
        var maxZ = maximumZ
        if (x < 0.0) minX -= x else if (x > 0.0) maxX -= x
        if (y < 0.0) minY -= y else if (y > 0.0) maxY -= y
        if (z < 0.0) minZ -= z else if (z > 0.0) maxZ -= z
        return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    /**
     * Returns true if this bounding box intersects with the given minimum and maximum
     * values, false otherwise.
     *
     * @param minimumX the minimum X value
     * @param minimumY the minimum Y value
     * @param minimumZ the minimum Z value
     * @param maximumX the maximum X value
     * @param maximumY the maximum Y value
     * @param maximumZ the maximum Z value
     * @return true if this box intersects with the values, false otherwise
     */
    fun intersects(minimumX: Double, minimumY: Double, minimumZ: Double, maximumX: Double, maximumY: Double, maximumZ: Double) =
        this.minimumX < maximumX && this.maximumX > minimumX &&
                this.minimumY < maximumY && this.maximumY > minimumY &&
                this.minimumZ < maximumZ && this.maximumZ > minimumZ

    /**
     * Returns true if this bounding box intersects with the given [other] bounding box,
     * false otherwise.
     *
     * @param other the other bounding box
     * @return true if this box intersects with the other box, false otherwise
     */
    fun intersects(other: BoundingBox) = intersects(other.minimumX, other.minimumY, other.minimumZ, other.maximumX, other.maximumY, other.maximumZ)

    /**
     * Returns true if the given [x], [y], and [z] values are inside the bounds of this
     * box, false otherwise.
     *
     * @param x the X value
     * @param y the Y value
     * @param z the Z value
     * @return true if this box contains the values, false otherwise
     */
    fun contains(x: Double, y: Double, z: Double) = x in minimumX..maximumX - 1 && y in minimumY..maximumY - 1 && z in minimumZ..maximumZ - 1

    /**
     * Returns true if the given [position] is inside the bounds of this box, false
     * otherwise.
     *
     * @param position the position
     * @return true if this box contains the position, false otherwise
     */
    operator fun contains(position: Position) = contains(position.x, position.y, position.z)

    companion object {

        /**
         * A bounding box with 0 for all of its values.
         */
        @JvmField
        val ZERO = BoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * A bounding box with 0 for all of its minimum values, and 1 for all of its maximum
         * values.
         */
        @JvmField
        val UNIT = BoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }
}
