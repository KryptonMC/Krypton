package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemManager
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.handler.DiamondSwordHandler
import org.kryptonmc.krypton.item.handler.GoldenSwordHandler
import org.kryptonmc.krypton.item.handler.IronSwordHandler
import org.kryptonmc.krypton.item.handler.NetheriteSwordHandler
import org.kryptonmc.krypton.item.handler.StoneSwordHandler
import org.kryptonmc.krypton.item.handler.TridentHandler
import org.kryptonmc.krypton.item.handler.WoodenSwordHandler

object KryptonItemManager : ItemManager {

    override val handlers = mutableMapOf<String, ItemHandler>()

    init {
        register(ItemTypes.WOODEN_SWORD.key.asString(), WoodenSwordHandler)
        register(ItemTypes.STONE_SWORD.key.asString(), StoneSwordHandler)
        register(ItemTypes.GOLDEN_SWORD.key.asString(), GoldenSwordHandler)
        register(ItemTypes.IRON_SWORD.key.asString(), IronSwordHandler)
        register(ItemTypes.DIAMOND_SWORD.key.asString(), DiamondSwordHandler)
        register(ItemTypes.NETHERITE_SWORD.key.asString(), NetheriteSwordHandler)
        register(ItemTypes.TRIDENT.key.asString(), TridentHandler)
    }

    override fun handler(key: String) = handlers[key]

    override fun register(key: String, handler: ItemHandler) {
        handlers[key] = handler
    }
}
