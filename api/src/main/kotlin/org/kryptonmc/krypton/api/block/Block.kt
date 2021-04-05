package org.kryptonmc.krypton.api.block

import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.chunk.Chunk

/**
 * Represents a block.
 *
 * @author Callum Seabrook
 */
interface Block {

    /**
     * The type of this block
     */
    val type: Material

    /**
     * This block's lighting values
     */
    val light: BlockLighting

    /**
     * The world this block is in
     */
    val world: World

    /**
     * The chunk this block is in
     */
    val chunk: Chunk

    /**
     * The location of this block
     */
    val location: Location

    /**
     * If this block is empty
     *
     * A block is defined as being empty if it's type is [Material.AIR]
     */
    val isEmpty: Boolean

    /**
     * If this block is a liquid
     *
     * A block is defined as being a liquid if its type is [Material.WATER] or [Material.LAVA]
     */
    val isLiquid: Boolean

    /**
     * This block's bounding box
     */
    val boundingBox: BoundingBox
}