package me.bristermitten.minekraft.world.block

import me.bristermitten.minekraft.entity.cardinal.Position

data class BlockPosition(
    override val x: Int,
    override val y: Int,
    override val z: Int
) : Position(x, y, z)