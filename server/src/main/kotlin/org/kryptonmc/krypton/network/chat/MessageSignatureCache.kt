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
package org.kryptonmc.krypton.network.chat

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.util.ArrayDeque
import java.util.Deque

class MessageSignatureCache(entryCount: Int) {

    private val entries = arrayOfNulls<MessageSignature>(entryCount)

    fun pack(signature: MessageSignature): Int = entries.indexOfFirst { it == signature }

    fun push(message: PlayerChatMessage) {
        val lastSeen = message.signedBody.lastSeen.entries
        val queue = ArrayDeque<MessageSignature>(lastSeen.size + 1)
        queue.addAll(lastSeen)
        message.signature?.let { queue.add(it) }
        push(queue)
    }

    private fun push(queue: Deque<MessageSignature>) {
        val signatures = ObjectOpenHashSet<MessageSignature>()
        var i = 0
        while (!queue.isEmpty() && i < entries.size) {
            val entry = entries[i]
            entries[i] = queue.removeLast()
            if (entry != null && !signatures.contains(entry)) queue.addFirst(entry)
            ++i
        }
    }

    companion object {

        private const val DEFAULT_CAPACITY = 128

        @JvmStatic
        fun createDefault(): MessageSignatureCache = MessageSignatureCache(DEFAULT_CAPACITY)
    }
}
