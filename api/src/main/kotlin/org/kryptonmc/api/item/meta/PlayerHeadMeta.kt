/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile
import javax.annotation.concurrent.Immutable

/**
 * Item metadata for a player head.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
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
