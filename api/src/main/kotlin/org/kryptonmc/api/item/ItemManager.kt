package org.kryptonmc.api.item

interface ItemManager {

    val handlers: Map<String, () -> ItemHandler>

    fun handler(key: String): ItemHandler?

    fun register(key: String, handler: () -> ItemHandler)
}
