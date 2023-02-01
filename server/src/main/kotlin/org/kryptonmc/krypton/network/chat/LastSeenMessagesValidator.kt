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
