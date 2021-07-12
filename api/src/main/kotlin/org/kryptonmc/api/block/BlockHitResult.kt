package org.kryptonmc.api.block

import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.util.HitResult
import org.spongepowered.math.vector.Vector3i

class BlockHitResult(
    clickLocation: Vector3i,
    val position: Vector3i,
    val direction: Direction,
    private val missed: Boolean,
    val isInside: Boolean
) : HitResult(clickLocation) {

    override val type = if (missed) Type.MISS else Type.BLOCK

    fun withPosition(position: Vector3i) = BlockHitResult(clickLocation, position, direction, missed, isInside)

    fun withDirection(direction: Direction) = BlockHitResult(clickLocation, position, direction, missed, isInside)
}
