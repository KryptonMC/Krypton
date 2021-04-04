package org.kryptonmc.krypton.api.inventory

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.inventory.item.ItemStack

/**
 * Represents a player's inventory
 *
 * This should follow the standard inventory of the protocol, which documents the following:
 * - The first slot, 0, is the crafting output slot
 * - Slots 1-4 are the crafting input slots
 * - Slots 5-8 are the armor slots (helmet, then chestplate, then leggings, then boots)
 * - Slots 9-35 are the main inventory (27 slots)
 * - Slots 36-44 are the player's hotbar (9 slots)
 * - Slot 45 is the offhand slot
 *
 * @author Callum Seabrook
 */
interface PlayerInventory : Inventory {

    /**
     * The elements of this inventory which are armour pieces
     */
    val armor: Array<ItemStack?>

    /**
     * The helmet this player is currently wearing, or null if this player
     * isn't wearing a helmet
     */
    val helmet: ItemStack?

    /**
     * The chestplate this player is currently wearing, or null if this
     * player isn't wearing a chestplate
     */
    val chestplate: ItemStack?

    /**
     * The leggings this player is currently wearing, or null if this
     * player isn't wearing any leggings
     */
    val leggings: ItemStack?

    /**
     * The boots this player is currently wearing, or null if this player
     * isn't wearing any boots
     */
    val boots: ItemStack?

    /**
     * The item that this player is currently holding in their main hand.
     *
     * Will be an [ItemStack] of type AIR and amount 0 if this player is
     * not holding anything in their main hand.
     */
    val mainHand: ItemStack

    /**
     * The item that this player is currently holding in their offhand.
     *
     * Will be an [ItemStack] of type AIR and amount 0 if this player is
     * not holding anything in their offhand.
     */
    val offHand: ItemStack

    /**
     * Gets the slot of the currently held item
     */
    val heldSlot: Int

    // TODO: Fix this when we fix the implementation and Player extends InventoryHolder again
//    override val owner: Player
}