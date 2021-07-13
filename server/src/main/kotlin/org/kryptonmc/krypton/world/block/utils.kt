package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockHandler
import org.kryptonmc.krypton.world.block.handler.DummyBlockHandler

val Block.handler: BlockHandler
    get() = KryptonBlockManager.handler(key.asString()) ?: DummyBlockHandler
