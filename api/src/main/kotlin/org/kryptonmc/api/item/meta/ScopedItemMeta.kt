/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.data.ItemFlag
import java.util.function.Consumer

/**
 * An item meta subtype that changes all of the returns of functions in item
 * meta to a generic type to avoid all subtypes having to override all of the
 * functions.
 */
public sealed interface ScopedItemMeta<B : ItemMetaBuilder<B, I>, I : ItemMeta> : ItemMeta, ItemMetaBuilder.Provider<B> {

    /**
     * Creates new item metadata from the result of applying the given
     * [builder].
     *
     * @param builder the builder function to apply
     * @return new item metadata
     */
    @JvmSynthetic
    @Contract("_ -> new", pure = true)
    public fun with(builder: B.() -> Unit): I = toBuilder().apply(builder).build()

    /**
     * Creates new item metadata from the result of applying the given
     * [builder].
     *
     * @param builder the builder function to apply
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun with(builder: Consumer<B>): I = with { builder.accept(this) }

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
