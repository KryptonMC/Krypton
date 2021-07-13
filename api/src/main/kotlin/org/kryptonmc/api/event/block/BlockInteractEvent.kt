package org.kryptonmc.api.event.block

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockFace
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.GenericResult

class BlockInteractEvent(
    val player: Player,
    val hand: Hand,
    val block: Block,
    val face: BlockFace
) {

    var useBlockResult = GenericResult.allowed()
    var useItemResult = GenericResult.allowed()

    fun deny() {
        useBlockResult = GenericResult.denied()
        useItemResult = GenericResult.denied()
    }
}
