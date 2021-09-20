package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.BlockHitResult
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.util.HitResult
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

object KryptonBlockHitResultFactory : BlockHitResult.Factory {

    override fun of(
        clickLocation: Vector3d,
        position: Vector3i,
        direction: Direction,
        missed: Boolean,
        isInside: Boolean
    ): BlockHitResult = KryptonBlockHitResult(
        clickLocation,
        if (missed) HitResult.Type.MISS else HitResult.Type.BLOCK,
        position,
        direction,
        isInside
    )
}
