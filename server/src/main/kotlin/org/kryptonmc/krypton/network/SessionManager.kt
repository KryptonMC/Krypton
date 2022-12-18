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

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.FramedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.PacketFraming
import org.kryptonmc.krypton.util.random.RandomSource
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate
import kotlin.math.min

class SessionManager(private val playerManager: PlayerManager, motd: Component, maxPlayers: Int) {

    private val sessions = ConcurrentHashMap.newKeySet<NettyConnection>()

    private val random = RandomSource.create()
    private val status = ServerStatus(motd, ServerStatus.Players(maxPlayers, playerManager.players.size), null)
    private var statusInvalidated = false
    private var statusInvalidatedTime = 0L
    private var lastStatus = 0L

    fun status(): ServerStatus = status

    fun register(session: NettyConnection) {
        sessions.add(session)
    }

    fun unregister(session: NettyConnection) {
        sessions.remove(session)
    }

    fun sendGrouped(packet: Packet) {
        sendGrouped(packet, ALWAYS_TRUE)
    }

    fun sendGrouped(packet: Packet, predicate: Predicate<KryptonPlayer>) {
        sendGrouped(playerManager.players, packet, predicate)
    }

    fun sendGrouped(players: Collection<KryptonPlayer>, packet: Packet) {
        sendGrouped(players, packet, ALWAYS_TRUE)
    }

    fun sendGrouped(players: Collection<KryptonPlayer>, packet: Packet, predicate: Predicate<KryptonPlayer>) {
        if (players.isEmpty()) return
        val finalBuffer = PacketFraming.frame(packet)
        val framedPacket = FramedPacket(finalBuffer)
        players.forEach { if (it.isOnline && predicate.test(it)) it.connection.write(framedPacket) }
        finalBuffer.release()
    }

    fun tick(time: Long) {
        if (statusInvalidated && time - statusInvalidatedTime > WAIT_AFTER_INVALID_STATUS_TIME || time - lastStatus >= UPDATE_STATUS_INTERVAL) {
            updateStatus(time)
        }
        sessions.forEach { it.tick() }
    }

    private fun updateStatus(time: Long) {
        lastStatus = time
        statusInvalidated = false
        statusInvalidatedTime = 0L
        val playersOnline = playerManager.players.size
        status.players.online = playersOnline
        val sampleSize = min(playersOnline, MAXIMUM_SAMPLED_PLAYERS)
        val playerOffset = Maths.nextInt(random, 0, playersOnline - sampleSize)
        val sample = Array(sampleSize) { playerManager.players.get(it + playerOffset).profile }.apply { shuffle() }
        status.players.sample = sample
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
        private val ALWAYS_TRUE = Predicate<KryptonPlayer> { true }
    }
}
