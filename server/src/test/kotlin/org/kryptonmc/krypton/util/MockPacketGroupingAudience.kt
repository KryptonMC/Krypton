/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import org.kryptonmc.krypton.adventure.GroupedPacketSender
import org.kryptonmc.krypton.adventure.NetworkAudienceMember
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.network.chat.ChatType
import org.kryptonmc.krypton.packet.Packet
import java.util.function.Predicate
import kotlin.test.assertTrue

class MockPacketGroupingAudience<M : NetworkAudienceMember>(override val members: Collection<M>) : PacketGroupingAudience<M> {

    val sentPackets: MutableList<Packet> = ArrayList()
    override val sender: GroupedPacketSender<M> = Sender()

    override fun sendGroupedPacket(packet: Packet, predicate: Predicate<M>) {
        checkSend(packet, predicate)
    }

    override fun acceptsChatType(member: M, chatType: ChatType): Boolean = true

    private fun checkSend(packet: Packet, predicate: Predicate<M>) {
        assertTrue(members.all(predicate::test), "All players part of the grouping audience should be sent the packet!")
        sentPackets.add(packet)
    }

    private inner class Sender : GroupedPacketSender<M> {

        override fun sendGrouped(packet: Packet, predicate: Predicate<M>) {
            checkSend(packet, predicate)
        }

        override fun sendGrouped(members: Collection<M>, packet: Packet, predicate: Predicate<M>) {
            checkSend(packet, predicate)
        }
    }
}
