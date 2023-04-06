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
package org.kryptonmc.api.entity.player

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.world.GameMode
import java.util.UUID

/**
 * An entry in a tab list.
 */
public interface TabListEntry {

    /**
     * The tab list this entry is registered to.
     */
    public val tabList: TabList

    /**
     * The UUID of the entry.
     */
    public val uuid: UUID

    /**
     * The game profile used to determine the name, UUID, and displayed skin
     * of the entry.
     */
    public val profile: GameProfile

    /**
     * The display name of the entry on the list.
     */
    public var displayName: Component?

    /**
     * The displayed game mode of the entry.
     */
    public var gameMode: GameMode

    /**
     * The displayed latency, or ping, of the entry.
     */
    public var latency: Int

    /**
     * Whether the entry is displayed on the list.
     */
    public var listed: Boolean

    /**
     * A builder for building tab list entries.
     */
    public interface Builder {

        /**
         * Sets the display name of the entry to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun displayName(name: Component?): Builder

        /**
         * Sets the game mode of the entry to the given [mode].
         *
         * @param mode the game mode
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun gameMode(mode: GameMode): Builder

        /**
         * Sets the latency of the entry to the given [latency].
         *
         * @param latency the latency
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun latency(latency: Int): Builder

        /**
         * Sets whether the entry is displayed on the list to the
         * given [value].
         *
         * @param value the value of the setting
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun listed(value: Boolean): Builder

        /**
         * Builds the new tab list entry and registers it to the tab list that
         * the builder was created from.
         *
         * @return the built entry
         */
        public fun buildAndRegister(): TabListEntry
    }
}
