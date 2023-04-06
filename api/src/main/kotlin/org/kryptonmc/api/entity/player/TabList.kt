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
import org.kryptonmc.api.auth.GameProfile
import java.util.UUID

/**
 * A tab list that is used to display information to players.
 *
 * While the proper name for it is the player list, it is commonly referred to
 * as the tab list, due to the fact that it is generally displayed by pressing
 * the tab key.
 */
public interface TabList {

    /**
     * The header of the tab list, shown above the entries.
     */
    public val header: Component

    /**
     * The footer of the tab list, shown below the entries.
     */
    public val footer: Component

    /**
     * The entries shown in the tab list.
     */
    public val entries: Collection<TabListEntry>

    /**
     * Sets the header and footer of the tab list to the given [header]
     * and [footer] values.
     *
     * To reset the header or footer, use [Component.empty].
     *
     * @param header the header
     * @param footer the footer
     */
    public fun setHeaderAndFooter(header: Component, footer: Component)

    /**
     * Gets the entry with the given [uuid], if it exists.
     *
     * @param uuid the uuid
     * @return the entry, or null if not present
     */
    public fun getEntry(uuid: UUID): TabListEntry?

    /**
     * Creates a new builder for building a tab list entry.
     *
     * @param uuid the uuid
     * @param profile the game profile
     * @return a new builder
     * @throws IllegalArgumentException if there is already an entry with the
     * same UUID
     */
    public fun createEntryBuilder(uuid: UUID, profile: GameProfile): TabListEntry.Builder

    /**
     * Removes the entry with the given [uuid] from the tab list, returning
     * whether the entry was removed successfully or not.
     *
     * If no entry with the given UUID exists, this will return false.
     *
     * @param uuid the uuid
     * @return the result of removing the entry
     */
    public fun removeEntry(uuid: UUID): Boolean
}
