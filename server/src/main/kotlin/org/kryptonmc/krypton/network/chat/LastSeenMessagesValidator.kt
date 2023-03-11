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

import it.unimi.dsi.fastutil.objects.ObjectArrayList

class LastSeenMessagesValidator(private val lastSeenCount: Int) {

    private val trackedMessages = ObjectArrayList<LastSeenTrackedEntry?>()
    private var lastPendingMessage: MessageSignature? = null

    init {
        for (i in 0 until lastSeenCount) {
            trackedMessages.add(null)
        }
    }

    fun addPending(signature: MessageSignature) {
        if (signature == lastPendingMessage) return
        trackedMessages.add(LastSeenTrackedEntry(signature, true))
        lastPendingMessage = signature
    }

    fun trackedMessagesCount(): Int = trackedMessages.size

    fun applyOffset(offset: Int): Boolean {
        val trackedOffset = trackedMessages.size - lastSeenCount
        if (offset >= 0 && offset <= trackedOffset) {
            trackedMessages.removeElements(0, offset)
            return true
        } else {
            return false
        }
    }

    fun applyUpdate(update: LastSeenMessages.Update): LastSeenMessages? {
        if (!applyOffset(update.offset)) return null
        if (update.acknowledged.length() > lastSeenCount) return null

        val entries = ObjectArrayList<MessageSignature>(update.acknowledged.cardinality())
        for (i in 0 until lastSeenCount) {
            val isAcknowledged = update.acknowledged.get(i)
            val entry = trackedMessages.get(i)
            if (isAcknowledged) {
                if (entry == null) return null
                trackedMessages.set(i, entry.acknowledge())
                entries.add(entry.signature)
            } else {
                if (entry != null && !entry.pending) return null
                trackedMessages.set(i, null)
            }
        }
        return LastSeenMessages(entries)
    }
}
