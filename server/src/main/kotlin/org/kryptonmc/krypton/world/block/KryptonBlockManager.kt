package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.BlockHandler
import org.kryptonmc.api.block.BlockManager

object KryptonBlockManager : BlockManager {

    override val handlers = mutableMapOf<String, BlockHandler>()

    override fun handler(key: String) = handlers[key]

    override fun register(key: String, handler: BlockHandler) {
        handlers[key] = handler
    }
}
