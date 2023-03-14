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

import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.util.ComponentException
import java.time.Instant
import java.util.UUID

class SignedMessageChain(sender: UUID, sessionId: UUID) {

    private var nextLink: SignedMessageLink? = SignedMessageLink.root(sender, sessionId)

    fun decoder(key: PlayerPublicKey): Decoder {
        val validator = key.createSignatureValidator()
        return Decoder { signature, body ->
            val advanced = advanceLink() ?: throw DecodeException(Component.translatable("chat.disabled.chain_broken"), false)
            if (key.data.hasExpired()) throw DecodeException(Component.translatable("chat.disabled.expiredProfileKey"), false)
            val message = PlayerChatMessage(advanced, signature, body, null, FilterMask.PASS_THROUGH)
            if (!message.verify(validator)) throw DecodeException(Component.translatable("multiplayer.disconnect.unsigned_chat"), true)
            if (message.hasExpired(Instant.now())) LOGGER.warn("Received expired chat message ${body.content}!")
            message
        }
    }

    private fun advanceLink(): SignedMessageLink? {
        val old = nextLink
        if (old != null) nextLink = old.advance()
        return old
    }

    class DecodeException(message: Component, val shouldDisconnect: Boolean) : ComponentException(message)

    fun interface Decoder {

        fun unpack(signature: MessageSignature?, body: SignedMessageBody): PlayerChatMessage

        companion object {

            @JvmField
            val REJECT_ALL: Decoder = Decoder { _, _ -> throw DecodeException(Component.translatable("chat.disabled.missingProfileKey"), false) }

            @JvmStatic
            fun unsigned(sender: UUID): Decoder = Decoder { _, body -> PlayerChatMessage.unsigned(sender, body.content) }
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
