package org.kryptonmc.krypton.world

import org.kryptonmc.api.block.Block

interface BlockAccessor : HeightAccessor {

    val maximumLightLevel: Int
        get() = 15

    fun getBlock(x: Int, y: Int, z: Int): Block

    fun setBlock(x: Int, y: Int, z: Int, block: Block)
}
