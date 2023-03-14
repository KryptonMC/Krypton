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
