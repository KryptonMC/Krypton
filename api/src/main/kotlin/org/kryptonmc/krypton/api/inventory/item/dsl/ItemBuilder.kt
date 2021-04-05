package org.kryptonmc.krypton.api.inventory.item.dsl

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.inventory.item.meta.ItemMeta

/**
 * Builder for building [ItemStack]s
 *
 * @author Callum Seabrook
 */
@Suppress("unused")
class ItemBuilder internal constructor(
    private var type: Material = Material.AIR,
    private var amount: Int = 1,
    private var name: Component? = null,
    private var lore: List<Component> = emptyList()
) {

    /**
     * Sets the type of this item
     *
     * @param type the type
     * @return this builder, with the type set to the provided type
     */
    fun type(type: Material) = apply { this.type = type }

    /**
     * Sets the amount of items in this stack
     *
     * @param amount the amount
     * @return this builder, with the new amount set
     * @throws IllegalArgumentException if the provided [amount] is not
     * between 0 and 64
     */
    fun amount(amount: Int) = apply {
        require(amount in 0..64) { "Amount must be between 0 and 64!" }
        this.amount = amount
    }

    /**
     * Sets the name of this item
     *
     * @param name the name
     * @return this builder, with the new name set
     */
    fun name(name: Component) = apply { this.name = name }

    /**
     * Sets the lore of this item
     *
     * @param lore the lore
     * @return this builder, with the new lore set
     */
    fun lore(lore: List<Component>) = apply { this.lore = lore }

    /**
     * Adds an item to the existing lore
     *
     * @param lore the lore item to add
     * @return this builder, with the item added to the lore
     */
    fun addLore(lore: Component) = apply { this.lore += lore }

    /**
     * Build this builder
     *
     * @return the built item stack, with the provided values applied
     */
    fun build() = ItemStack(type, amount, BuiltItemMeta(name, lore))
}

// likely won't work forever, since different items will have different meta,
// but will work for now
private class BuiltItemMeta(
    override val displayName: Component?,
    override val lore: List<Component>
) : ItemMeta