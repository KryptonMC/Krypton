package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemManager
import org.kryptonmc.krypton.item.handler.DummyItemHandler

object KryptonItemManager : ItemManager {

    override val handlers = mutableMapOf<String, () -> ItemHandler>()

    override fun handler(key: String) = handlers[key]?.invoke()

    override fun register(key: String, handler: () -> ItemHandler) {
        handlers[key] = handler
    }
}
