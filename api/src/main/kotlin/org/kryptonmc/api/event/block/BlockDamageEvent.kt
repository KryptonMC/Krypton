package org.kryptonmc.api.event.block

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent

class BlockDamageEvent(
    val player: Player,
    val block: Block,
    var instantlyBreaks: Boolean
) : ResultedEvent<GenericResult> {

    override var result = GenericResult.allowed()
}
