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

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.writeUUID
import java.time.Duration
import java.util.UUID

@JvmRecord
data class RemoteChatSession(val sessionId: UUID, val publicKey: PlayerPublicKey) {

    fun createMessageValidator(): SignedMessageValidator = SignedMessageValidator.KeyBased(publicKey.createSignatureValidator())

    fun createMessageDecoder(id: UUID): SignedMessageChain.Decoder = SignedMessageChain(id, sessionId).decoder(publicKey)

    fun asData(): Data = Data(sessionId, publicKey.data)

    @JvmRecord
    data class Data(val sessionId: UUID, val publicKey: PlayerPublicKey.Data) {

        fun validate(profile: GameProfile, validator: SignatureValidator, duration: Duration): RemoteChatSession =
            RemoteChatSession(sessionId, PlayerPublicKey.createValidated(validator, profile.uuid, publicKey, duration))

        companion object {

            @JvmStatic
            fun read(buf: ByteBuf): Data = Data(buf.readUUID(), PlayerPublicKey.Data(buf))

            @JvmStatic
            fun write(buf: ByteBuf, session: Data) {
                buf.writeUUID(session.sessionId)
                session.publicKey.write(buf)
            }
        }
    }
}
