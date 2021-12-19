package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.ItemFlag

/**
 * The base builder for item metadata.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemMetaBuilder<B : ItemMetaBuilder<B, I>, I : ItemMeta> {

    /**
     * Sets the damage of the item to the given [damage].
     *
     * @param damage the damage
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun damage(damage: Int): B

    /**
     * Sets whether the item is unbreakable to the given [value].
     *
     * @param value whether the item is unbreakable
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun unbreakable(value: Boolean): B

    /**
     * Makes the item unbreakable.
     *
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun unbreakable(): B = unbreakable(true)

    /**
     * Sets the custom model data for the item to the given [data].
     *
     * @param data the custom model data
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun customModelData(data: Int): B

    /**
     * Sets the name of the item to the given [name].
     *
     * @param name the name
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun name(name: Component?): B

    /**
     * Sets the lore of the item to the given [lore].
     *
     * @param lore the lore
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun lore(lore: Iterable<Component>): B

    /**
     * Sets the lore of the item to the given [lore].
     *
     * @param lore the lore
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun lore(vararg lore: Component): B

    /**
     * Sets the lore of the item to the given [lore].
     *
     * @param lore the lore
     * @return this builder
     */
    @JvmSynthetic
    @JvmName("loreArray")
    @Contract("_ -> this", mutates = "this")
    public fun lore(lore: Array<Component>): B

    /**
     * Adds the given [lore] line to the lore of the item.
     *
     * @param lore the lore line to add
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun addLore(lore: Component): B

    /**
     * Sets the hide flags for the item to the given [flags].
     *
     * @param flags the hide flags
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun hideFlags(flags: Int): B

    /**
     * Sets the given hide [flag] on the item.
     *
     * @param flag the hide flag to set
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun addFlag(flag: ItemFlag): B

    /**
     * Sets the list of blocks the item can destroy to the given [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun canDestroy(blocks: Iterable<Block>): B

    /**
     * Sets the list of blocks the item can destroy to the given [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun canDestroy(vararg blocks: Block): B

    /**
     * Sets the list of blocks the item can destroy to the given [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @JvmSynthetic
    @JvmName("canDestroyArray")
    @Contract("_ -> this", mutates = "this")
    public fun canDestroy(blocks: Array<Block>): B

    /**
     * Adds the given [block] to the list of blocks the item can destroy.
     *
     * @param block the block to add
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun addCanDestroy(block: Block): B

    /**
     * Sets the list of blocks the item can be placed on to the given
     * [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun canPlaceOn(blocks: Iterable<Block>): B

    /**
     * Sets the list of blocks the item can be placed on to the given
     * [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun canPlaceOn(vararg blocks: Block): B

    /**
     * Sets the list of blocks the item can be placed on to the given
     * [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @JvmSynthetic
    @JvmName("canPlaceOnArray")
    @Contract("_ -> this", mutates = "this")
    public fun canPlaceOn(blocks: Array<Block>): B

    /**
     * Adds the given [block] to the list of blocks the item can be placed on.
     *
     * @param block the block to add
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun addCanPlaceOn(block: Block): B

    /**
     * Builds the resulting item metadata.
     *
     * @return the built item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun build(): I
}
