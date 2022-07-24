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
package org.kryptonmc.krypton.network

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.GroupedPacketSender
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.handlers.PlayHandler
import org.kryptonmc.krypton.packet.FramedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.util.frame
import org.kryptonmc.krypton.util.nextIntClamped
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Predicate
import kotlin.concurrent.write
import kotlin.math.min
import kotlin.random.Random

class SessionManager(private val server: KryptonServer) : GroupedPacketSender<KryptonPlayer> {

    private val sessions = ConcurrentHashMap.newKeySet<SessionHandler>()
    private val lock = ReentrantReadWriteLock()

    val status: ServerStatus = ServerStatus(server.motd, ServerStatus.Players(server.maxPlayers, server.playerManager.players.size), null)
    private var statusInvalidated = false
    private var statusInvalidatedTime = 0L
    private var lastStatus = 0L

    fun add(session: SessionHandler) {
        lock.write { sessions.add(session) }
    }

    fun remove(session: SessionHandler) {
        lock.write { sessions.remove(session) }
    }

    override fun sendGrouped(packet: Packet, predicate: Predicate<KryptonPlayer>) {
        sendGrouped(server.playerManager.players, packet, predicate)
    }

    override fun sendGrouped(members: Collection<KryptonPlayer>, packet: Packet, predicate: Predicate<KryptonPlayer>) {
        if (members.isEmpty()) return
        val finalBuffer = packet.frame()
        val framedPacket = FramedPacket(finalBuffer)
        members.forEach {
            if (!it.isOnline || !predicate.test(it)) return@forEach
            it.session.write(framedPacket)
        }
        finalBuffer.release()
    }

    fun update(time: Long) {
        if ((statusInvalidated && time - statusInvalidatedTime > WAIT_AFTER_INVALID_STATUS_TIME) || time - lastStatus >= UPDATE_STATUS_INTERVAL) {
            lastStatus = time
            statusInvalidated = false
            statusInvalidatedTime = 0L
            val playersOnline = server.playerManager.players.size
            status.players.online = playersOnline
            val sampleSize = min(playersOnline, MAXIMUM_SAMPLED_PLAYERS)
            val playerOffset = Random.nextIntClamped(0, playersOnline - sampleSize)
            val sample = Array(sampleSize) { server.playerManager.players[it + playerOffset].profile }.apply { shuffle() }
            status.players.sample = sample
        }
        sessions.forEach {
            val handler = it.handler
            if (handler is PlayHandler) handler.tick()
            it.flush()
        }
    }

    fun invalidateStatus() {
        if (statusInvalidated) return
        statusInvalidated = true
        statusInvalidatedTime = System.currentTimeMillis()
        lastStatus = 0L
    }

    companion object {

        private const val UPDATE_STATUS_INTERVAL = 5000L
        private const val WAIT_AFTER_INVALID_STATUS_TIME = 1000L
        private const val MAXIMUM_SAMPLED_PLAYERS = 12
    }
}
