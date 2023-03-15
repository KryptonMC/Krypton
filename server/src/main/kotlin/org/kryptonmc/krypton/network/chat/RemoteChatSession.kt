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

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.util.crypto.SignatureValidator
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
            fun read(reader: BinaryReader): Data = Data(reader.readUUID(), PlayerPublicKey.Data(reader))

            @JvmStatic
            fun write(writer: BinaryWriter, session: Data) {
                writer.writeUUID(session.sessionId)
                session.publicKey.write(writer)
            }
        }
    }
}
