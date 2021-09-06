/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.chunk.ticket

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.SortedArraySet
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max

class TicketManager(private val chunkManager: ChunkManager) {

    private val tickets: Long2ObjectMap<SortedArraySet<Ticket<*>>> = Long2ObjectMaps.synchronize(
        Long2ObjectOpenHashMap()
    )

    fun <T> addTicket(x: Int, z: Int, type: TicketType<T>, level: Int, key: T, onLoad: () -> Unit) {
        if (type === TicketTypes.PLAYER) return
        propagate(x, z, Ticket(type, level, key), onLoad)
    }

    fun <T> removeTicket(x: Int, z: Int, type: TicketType<T>, level: Int, key: T) {
        if (type === TicketTypes.PLAYER) return
        val ticket = Ticket(type, level, key)
        tickets[ChunkPosition.toLong(x, z)]?.remove(ticket)
        val radius = MAXIMUM_TICKET_LEVEL - level + 1
        reset(x, z, type, key, radius)
    }

    fun addPlayer(x: Int, z: Int, oldX: Int, oldZ: Int, uuid: UUID, viewDistance: Int) {
        if (x != oldX || z != oldZ) reset(oldX, oldZ, TicketTypes.PLAYER, uuid, viewDistance, OFFSET, false)
        propagateView(x, z, uuid, viewDistance)
    }

    fun removePlayer(x: Int, z: Int, uuid: UUID, viewDistance: Int) = reset(
        x,
        z,
        TicketTypes.PLAYER,
        uuid,
        viewDistance,
        OFFSET
    )

    private fun <T> propagate(x: Int, z: Int, ticket: Ticket<T>, onLoad: () -> Unit) {
        var i = 0
        while (true) {
            val pos = Maths.chunkInSpiral(i, x, z)
            val xo = pos.toInt()
            val zo = (pos shr 32).toInt()
            val calculatedLevel = calculateLevel(absDelta(xo - x, zo - z), ticket.level)
            if (calculatedLevel > MAXIMUM_TICKET_LEVEL) break
            val newTicket = Ticket(ticket.type, calculatedLevel, ticket.key)
            tickets.getOrPut(pos) { SortedArraySet.create(4) }.add(newTicket)
            if (calculatedLevel <= PLAYER_TICKET_LEVEL) {
                chunkManager.load(xo, zo, newTicket)
                onLoad()
            }
            i++
        }
    }

    private fun propagateView(x: Int, z: Int, uuid: UUID, viewDistance: Int) {
        var i = 0
        while (true) {
            val pos = Maths.chunkInSpiral(i, x, z)
            val xo = pos.toInt()
            val zo = (pos shr 32).toInt()
            val absDelta = absDelta(xo - x, zo - z)
            val calculatedLevel = if (absDelta <= viewDistance) {
                PLAYER_TICKET_LEVEL
            } else {
                calculateLevel(absDelta - viewDistance, PLAYER_TICKET_LEVEL)
            }
            if (calculatedLevel > MAXIMUM_TICKET_LEVEL) break
            val ticket = Ticket(TicketTypes.PLAYER, calculatedLevel, uuid)
            tickets.getOrPut(pos) { SortedArraySet.create(4) }.add(ticket)
            if (calculatedLevel <= PLAYER_TICKET_LEVEL) chunkManager.load(xo, zo, ticket)
            i++
        }
    }

    private fun <T, K> reset(
        x: Int,
        z: Int,
        type: TicketType<T>,
        key: K,
        radius: Int,
        offset: Int = 0,
        shouldUnload: Boolean = true
    ) {
        for (i in 0 until (radius * 2 + offset) * (radius * 2 + offset)) {
            val pos = Maths.chunkInSpiral(i, x, z)
            val list = tickets[pos] ?: continue
            list.removeIf { it.type === type && it.key === key }
            if (list.isEmpty()) {
                tickets.remove(pos)
                if (shouldUnload) chunkManager.chunkMap.remove(pos)
            }
        }
    }

    companion object {

        private const val PLAYER_TICKET_LEVEL = 31
        private const val MAXIMUM_TICKET_LEVEL = 44
        private const val OFFSET = (MAXIMUM_TICKET_LEVEL - PLAYER_TICKET_LEVEL) * 2 + 1

        private fun absDelta(deltaX: Int, deltaZ: Int): Int {
            if (deltaX == 0 && deltaZ == 0) return 0
            return max(abs(deltaX), abs(deltaZ))
        }

        private fun calculateLevel(absDelta: Int, center: Int): Int = if (absDelta >= 0) center + absDelta else center - absDelta
    }
}
