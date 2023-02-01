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

import com.google.common.primitives.Ints
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import java.util.UUID

@JvmRecord
data class SignedMessageLink(val index: Int, val sender: UUID, val sessionId: UUID) {

    fun updateSignature(output: SignatureUpdater.Output) {
        output.update(UUIDUtil.toByteArray(sender))
        output.update(UUIDUtil.toByteArray(sessionId))
        output.update(Ints.toByteArray(index))
    }

    fun isDescendantOf(link: SignedMessageLink): Boolean = index > link.index && sender == link.sender && sessionId == link.sessionId

    fun advance(): SignedMessageLink? = if (index == Int.MAX_VALUE) null else SignedMessageLink(index + 1, sender, sessionId)

    companion object {

        @JvmStatic
        fun unsigned(sender: UUID): SignedMessageLink = root(sender, UUIDUtil.NIL_UUID)

        @JvmStatic
        fun root(sender: UUID, sessionId: UUID): SignedMessageLink = SignedMessageLink(0, sender, sessionId)
    }
}
