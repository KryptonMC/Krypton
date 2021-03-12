package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.world.chunk.KryptonChunk

data class Region(
    val x: Int,
    val z: Int,
    val chunks: Map<Vector, KryptonChunk>
)