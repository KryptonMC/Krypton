package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.item.handler.DummyItemHandler

val ItemType.handler: ItemHandler
    get() = KryptonItemManager.handler(key.asString()) ?: DummyItemHandler
