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
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a crossbow.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface CrossbowMeta : ScopedItemMeta<CrossbowMeta.Builder, CrossbowMeta> {

    /**
     * If the crossbow is charged.
     */
    public val isCharged: Boolean

    /**
     * The projectiles that the crossbow has charged.
     */
    @get:JvmName("projectiles")
    public val projectiles: @Unmodifiable List<ItemStack>

    /**
     * Creates new item metadata with the given [charged] setting.
     *
     * @param charged the new charged setting
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withCharged(charged: Boolean): CrossbowMeta

    /**
     * Creates new item metadata with the given [projectiles].
     *
     * @param projectiles the new projectiles
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withProjectiles(projectiles: List<ItemStack>): CrossbowMeta

    /**
     * Creates new item metadata with the given [projectile] added to the end
     * of the projectiles list.
     *
     * @param projectile the projectile to add
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withProjectile(projectile: ItemStack): CrossbowMeta

    /**
     * Creates new item metadata with the projectile at the given [index]
     * removed from the projectiles list.
     *
     * @param index the index of the projectile to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    @Contract("_ -> new", pure = true)
    public fun withoutProjectile(index: Int): CrossbowMeta

    /**
     * Creates new item metadata with the given [projectile] removed from the
     * projectiles list.
     *
     * @param projectile the projectile to remove
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutProjectile(projectile: ItemStack): CrossbowMeta

    /**
     * A builder for building crossbow metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, CrossbowMeta> {

        /**
         * Sets whether the crossbow is charged to the given [value].
         *
         * @param value whether the crossbow is charged
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun charged(value: Boolean): Builder

        /**
         * Sets the list of charged projectiles for the crossbow to the given
         * [projectiles].
         *
         * @param projectiles the projectiles
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun projectiles(projectiles: Collection<ItemStack>): Builder

        /**
         * Sets the list of charged projectiles for the crossbow to the given
         * [projectiles].
         *
         * @param projectiles the projectiles
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun projectiles(vararg projectiles: ItemStack): Builder = projectiles(projectiles.asList())
    }

    public companion object {

        /**
         * Creates a new builder for building crossbow metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(CrossbowMeta::class.java)
    }
}
