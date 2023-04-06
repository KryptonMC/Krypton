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
import org.kryptonmc.api.entity.player.TabList
import org.kryptonmc.api.entity.player.TabListEntry
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoRemove
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate.Action as PacketAction
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate.Entry as PacketEntry
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTabListHeaderAndFooter
import org.kryptonmc.krypton.util.ImmutableLists
import java.util.Collections
import java.util.EnumSet
import java.util.UUID

class KryptonTabList(private val player: KryptonPlayer) : TabList {

    override var header: Component = Component.empty()
    override var footer: Component = Component.empty()

    private val entryMap = HashMap<UUID, TabListEntry>()
    override val entries: Collection<TabListEntry>
        get() = Collections.unmodifiableCollection(entryMap.values)

    override fun setHeaderAndFooter(header: Component, footer: Component) {
        this.header = header
        this.footer = footer
        player.connection.send(PacketOutSetTabListHeaderAndFooter(header, footer))
    }

    override fun getEntry(uuid: UUID): TabListEntry? = entryMap.get(uuid)

    override fun createEntryBuilder(uuid: UUID, profile: GameProfile): TabListEntry.Builder {
        require(getEntry(uuid) == null) { "An entry with UUID $uuid already exists on this list!" }
        return KryptonTabListEntry.Builder(this, uuid, profile)
    }

    fun addEntry(entry: KryptonTabListEntry) {
        entryMap.put(entry.uuid, entry)
        sendAdd(entry)
    }

    override fun removeEntry(uuid: UUID): Boolean {
        entryMap.remove(uuid) ?: return false
        sendRemove(uuid)
        return true
    }

    private fun sendAdd(entry: TabListEntry) {
        val packetEntry = PacketEntry(entry.uuid, entry.profile, entry.listed, entry.latency, entry.gameMode, entry.displayName, null)
        val packet = PacketOutPlayerInfoUpdate(EnumSet.allOf(PacketAction::class.java), ImmutableLists.of(packetEntry))
        player.connection.send(packet)
    }

    private fun sendRemove(uuid: UUID) {
        player.connection.send(PacketOutPlayerInfoRemove(ImmutableLists.of(uuid)))
    }

    fun updateEntry(action: PacketAction, entry: TabListEntry) {
        val packetEntry = PacketEntry(entry.uuid, entry.profile, entry.listed, entry.latency, entry.gameMode, entry.displayName, null)
        val packet = PacketOutPlayerInfoUpdate(EnumSet.of(action), ImmutableLists.of(packetEntry))
        player.connection.send(packet)
    }
}
