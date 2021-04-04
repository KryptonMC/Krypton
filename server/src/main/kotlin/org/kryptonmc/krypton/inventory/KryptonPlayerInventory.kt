package org.kryptonmc.krypton.inventory

import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.PlayerInventory
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutHeldItemChange

class KryptonPlayerInventory(override val owner: KryptonPlayer) : KryptonInventory(TYPE, owner, SIZE), PlayerInventory {

    override val crafting: Array<ItemStack?>
        get() = items.copyOfRange(0, 5)

    override val armor: Array<ItemStack?>
        get() = items.copyOfRange(5, 9)

    override val main: Array<ItemStack?>
        get() = items.copyOfRange(9, 36)

    override val hotbar: Array<ItemStack?>
        get() = items.copyOfRange(36, 45)

    override val helmet: ItemStack?
        get() = armor[0]
    override val chestplate: ItemStack?
        get() = armor[1]
    override val leggings: ItemStack?
        get() = armor[2]
    override val boots: ItemStack?
        get() = armor[3]

    override var heldSlot = 0

    override val mainHand: ItemStack?
        get() = hotbar[heldSlot]

    override val offHand: ItemStack?
        get() = items[45]

    override fun set(index: Int, item: ItemStack) {
        super.set(index, item)

        if (index == heldSlot) return
        owner.session.sendPacket(PacketOutHeldItemChange(index))
    }

    internal fun populate(from: Int, to: Int, itemList: List<ItemStack>) {
        if (itemList.isEmpty()) return
        for (i in from..to) {
            items[i] = itemList[i - from]
        }
    }

    companion object {

        private val TYPE = InventoryType.PLAYER
        private const val SIZE = 46
    }
}