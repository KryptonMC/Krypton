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
package org.kryptonmc.krypton.entity.player

import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.entity.player.TabListEntry
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate
import java.util.UUID

class KryptonTabListEntry(
    override val tabList: KryptonTabList,
    override val uuid: UUID,
    override val profile: GameProfile,
    displayName: Component?,
    gameMode: GameMode,
    latency: Int,
    listed: Boolean
) : TabListEntry {

    override var displayName: Component? = displayName
        set(value) {
            field = value
            tabList.updateEntry(PacketOutPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, this)
        }
    override var gameMode: GameMode = gameMode
        set(value) {
            field = value
            tabList.updateEntry(PacketOutPlayerInfoUpdate.Action.UPDATE_GAME_MODE, this)
        }
    override var latency: Int = latency
        set(value) {
            field = value
            tabList.updateEntry(PacketOutPlayerInfoUpdate.Action.UPDATE_LATENCY, this)
        }
    override var listed: Boolean = listed
        set(value) {
            field = value
            tabList.updateEntry(PacketOutPlayerInfoUpdate.Action.UPDATE_LISTED, this)
        }

    override fun equals(other: Any?): Boolean {
        return this === other || other is KryptonTabListEntry && uuid == other.uuid
    }

    override fun hashCode(): Int = uuid.hashCode()

    override fun toString(): String = "KryptonTabListEntry(uuid=$uuid, profile=$profile, displayName=$displayName, gameMode=$gameMode, " +
            "latency=$latency, listed=$listed)"

    class Builder(private val tabList: KryptonTabList, private val uuid: UUID, private val profile: GameProfile) : TabListEntry.Builder {

        private var displayName: Component? = null
        private var gameMode = GameMode.SURVIVAL
        private var latency = 0
        private var listed = false

        override fun displayName(name: Component?): Builder = apply { displayName = name }

        override fun gameMode(mode: GameMode): Builder = apply { gameMode = mode }

        override fun latency(latency: Int): Builder = apply { this.latency = latency }

        override fun listed(value: Boolean): Builder = apply { listed = value }

        override fun buildAndRegister(): TabListEntry {
            val entry = KryptonTabListEntry(tabList, uuid, profile, displayName, gameMode, latency, listed)
            tabList.addEntry(entry)
            return entry
        }
    }
}
