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
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a firework star.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface FireworkStarMeta : ScopedItemMeta<FireworkStarMeta.Builder, FireworkStarMeta> {

    /**
     * The effect displayed when the star explodes.
     */
    @get:JvmName("effect")
    public val effect: FireworkEffect?

    /**
     * Creates new item metadata with the given [effect].
     *
     * @param effect the new effect
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withEffect(effect: FireworkEffect?): FireworkStarMeta

    /**
     * A builder for building firework star metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, FireworkStarMeta> {

        /**
         * Sets the effect of the star to the given [effect].
         *
         * @param effect the effect
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun effect(effect: FireworkEffect?): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building bundle metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(FireworkStarMeta::class.java)
    }
}
