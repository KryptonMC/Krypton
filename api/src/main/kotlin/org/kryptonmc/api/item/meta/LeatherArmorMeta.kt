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

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.util.Color
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for leather armour.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface LeatherArmorMeta : ScopedItemMeta<LeatherArmorMeta.Builder, LeatherArmorMeta> {

    /**
     * The displayed colour of the item.
     */
    @get:JvmName("color")
    public val color: Color?

    /**
     * Creates new item metadata with the given [color].
     *
     * @param color the new colour
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withColor(color: Color?): LeatherArmorMeta

    /**
     * A builder for building leather armour metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, LeatherArmorMeta> {

        /**
         * Sets the colour of the leather armour to the given [color].
         *
         * @param color the colour
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun color(color: Color?): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building leather armour metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(LeatherArmorMeta::class.java)
    }
}
