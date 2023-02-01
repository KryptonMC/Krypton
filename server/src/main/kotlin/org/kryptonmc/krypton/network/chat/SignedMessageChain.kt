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
