package org.kryptonmc.krypton.world.block

import org.kryptonmc.krypton.entity.cardinal.Position

data class BlockPosition(
    override val x: Int,
    override val y: Int,
    override val z: Int
) : Position(x, y, z)