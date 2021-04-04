package org.kryptonmc.krypton.inventory

import org.kryptonmc.krypton.api.inventory.InventoryHolder
import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.PlayerInventory
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.api.inventory.item.ItemType
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutHeldItemChange

class KryptonPlayerInventory(override val owner: InventoryHolder) : KryptonInventory(TYPE, owner, SIZE), PlayerInventory {

    override val armor: Array<ItemStack?>
        get() = items.filter { it?.type in ARMOR }.toTypedArray()

    override var helmet: ItemStack? = get(5)
    override var chestplate: ItemStack? = get(6)
    override var leggings: ItemStack? = get(7)
    override var boots: ItemStack? = get(8)

    override var mainHand: ItemStack = EMPTY_STACK
    override var offHand: ItemStack = EMPTY_STACK

    override var heldSlot = 0

    override fun set(index: Int, item: ItemStack) {
        super.set(index, item)

        // TODO: Fix this when we fix inventories on the server's end
//        if (index == heldSlot) return
//        owner.session.sendPacket(PacketOutHeldItemChange(index))
    }

    companion object {

        private val TYPE = InventoryType.PLAYER
        private const val SIZE = 46
        private val EMPTY_STACK = ItemStack(ItemType.AIR, 0)
    }
}

private val HELMETS = setOf(
    ItemType.LEATHER_CAP,
    ItemType.CHAINMAIL_HELMET,
    ItemType.GOLDEN_HELMET,
    ItemType.IRON_HELMET,
    ItemType.DIAMOND_HELMET,
    ItemType.NETHERITE_HELMET
)

private val CHESTPLATES = setOf(
    ItemType.LEATHER_TUNIC,
    ItemType.CHAINMAIL_CHESTPLATE,
    ItemType.GOLDEN_CHESTPLATE,
    ItemType.IRON_CHESTPLATE,
    ItemType.DIAMOND_CHESTPLATE,
    ItemType.NETHERITE_CHESTPLATE
)

private val LEGGINGS = setOf(
    ItemType.LEATHER_PANTS,
    ItemType.CHAINMAIL_LEGGINGS,
    ItemType.GOLDEN_LEGGINGS,
    ItemType.IRON_LEGGINGS,
    ItemType.DIAMOND_LEGGINGS,
    ItemType.NETHERITE_LEGGINGS
)

private val BOOTS = setOf(
    ItemType.LEATHER_BOOTS,
    ItemType.CHAINMAIL_BOOTS,
    ItemType.GOLDEN_BOOTS,
    ItemType.IRON_BOOTS,
    ItemType.DIAMOND_BOOTS,
    ItemType.NETHERITE_BOOTS
)

private val ARMOR = HELMETS + CHESTPLATES + LEGGINGS + BOOTS