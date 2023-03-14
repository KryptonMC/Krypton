/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemAttributeModifier
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.internal.annotations.ImmutableType
import java.util.function.Consumer

/**
 * An item meta subtype that changes all of the returns of functions in item
 * meta to a generic type to avoid all subtypes having to override all of the
 * functions.
 */
@ImmutableType
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

    override fun withLore(lore: List<Component>): I

    override fun withLore(lore: Component): I

    override fun withoutLore(index: Int): I

    override fun withoutLore(lore: Component): I

    override fun withHideFlags(flags: Int): I

    override fun withHideFlag(flag: ItemFlag): I

    override fun withoutHideFlag(flag: ItemFlag): I

    override fun withCanDestroy(blocks: Collection<Block>): I

    override fun withCanPlaceOn(blocks: Collection<Block>): I

    override fun withAttributeModifiers(modifiers: Collection<ItemAttributeModifier>): I

    override fun withoutAttributeModifiers(): I

    override fun withAttributeModifier(modifier: ItemAttributeModifier): I

    override fun withoutAttributeModifier(modifier: ItemAttributeModifier): I
}
