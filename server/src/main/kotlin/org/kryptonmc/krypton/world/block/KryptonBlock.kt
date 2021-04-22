package org.kryptonmc.krypton.world.block

import org.kryptonmc.krypton.api.block.Block
import org.kryptonmc.krypton.api.block.BoundingBox
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.chunk.Chunk

data class KryptonBlock(
    override val type: Material,
    override val chunk: Chunk,
    override val location: Location
) : Block {

    override val isEmpty = type == Material.AIR
    override val isLiquid = type == Material.WATER || type == Material.LAVA
    override val world = chunk.world
    override val boundingBox = BoundingBox.EMPTY
}