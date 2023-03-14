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
