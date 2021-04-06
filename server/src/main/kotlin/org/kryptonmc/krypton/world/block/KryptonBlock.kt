package org.kryptonmc.krypton.world.block

import org.kryptonmc.krypton.api.block.Block
import org.kryptonmc.krypton.api.block.BoundingBox
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.chunk.Chunk

data class KryptonBlock(
    override val type: Material,
    override val world: World,
    override val chunk: Chunk,
    override val location: Location,
    override val boundingBox: BoundingBox
) : Block {

    override val isEmpty = type == Material.AIR

    override val isLiquid = type == Material.WATER || type == Material.LAVA
}