package org.kryptonmc.krypton.api.inventory

/**
 * Represents any object that can hold an inventory (e.g. entities, players, etc.)
 *
 * @author Callum Seabrook
 */
interface InventoryHolder {

    /**
     * The inventory that is being held by this holder
     */
    val inventory: Inventory
}