package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.BlockHitResult
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.util.HitResult
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class KryptonBlockHitResult(
    override val clickLocation: Vector3d,
    override val type: HitResult.Type,
    override val position: Vector3i,
    override val direction: Direction,
    override val isInside: Boolean
) : BlockHitResult
