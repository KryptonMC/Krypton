package org.kryptonmc.krypton.world.block.handler

import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.world.block.KryptonBlockManager

fun Block.handler() = KryptonBlockManager.handler(this) ?: DummyBlockHandler
