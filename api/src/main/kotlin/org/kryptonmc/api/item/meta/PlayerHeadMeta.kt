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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a player head.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface PlayerHeadMeta : ScopedItemMeta<PlayerHeadMeta.Builder, PlayerHeadMeta> {

    /**
     * The profile of the player that the head belongs to.
     */
    @get:JvmName("owner")
    public val owner: GameProfile?

    /**
     * Creates new item metadata with the given [owner].
     *
     * @param owner the new owner
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withOwner(owner: GameProfile?): PlayerHeadMeta

    /**
     * A builder for building player head metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, PlayerHeadMeta> {

        /**
         * Sets the owner of the player head to the given [owner].
         *
         * @param owner the owner
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun owner(owner: GameProfile?): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building player head metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(PlayerHeadMeta::class.java)
    }
}
