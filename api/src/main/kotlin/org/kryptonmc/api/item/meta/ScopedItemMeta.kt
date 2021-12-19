package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.ItemFlag

/**
 * An item meta subtype that changes all of the returns of functions in item
 * meta to a generic type to avoid all subtypes having to override all of the
 * functions.
 */
public sealed interface ScopedItemMeta<I : ItemMeta> : ItemMeta {

    override fun withDamage(damage: Int): I

    override fun withUnbreakable(unbreakable: Boolean): I

    override fun withCustomModelData(data: Int): I

    override fun withName(name: Component?): I

    override fun withLore(lore: Iterable<Component>): I

    override fun addLore(lore: Component): I

    override fun removeLore(index: Int): I

    override fun removeLore(lore: Component): I

    override fun withHideFlags(flags: Int): I

    override fun withHideFlag(flag: ItemFlag): I

    override fun withoutHideFlag(flag: ItemFlag): I

    override fun withCanDestroy(blocks: Iterable<Block>): I

    override fun withCanPlaceOn(blocks: Iterable<Block>): I
}
