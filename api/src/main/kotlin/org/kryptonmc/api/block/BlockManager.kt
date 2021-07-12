package org.kryptonmc.api.block

interface BlockManager {

    val handlers: Map<String, () -> BlockHandler>

    fun handler(key: String): BlockHandler?

    fun register(key: String, handler: () -> BlockHandler)
}
