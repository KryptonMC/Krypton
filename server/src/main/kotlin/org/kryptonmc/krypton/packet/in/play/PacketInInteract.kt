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
package org.kryptonmc.krypton.packet.`in`.play

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket

@JvmRecord
data class PacketInInteract(val entityId: Int, val action: Action, val sneaking: Boolean) : InboundPacket<PlayPacketHandler> {

    constructor(reader: BinaryReader) : this(reader.readVarInt(), reader.readEnum<ActionType>().read(reader), reader.readBoolean())

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(entityId)
        writer.writeEnum(action.type())
        action.write(writer)
        writer.writeBoolean(sneaking)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handleInteract(this)
    }

    sealed interface Action : Writable {

        fun type(): ActionType
    }

    @JvmRecord
    data class InteractAction(val hand: Hand) : Action {

        constructor(reader: BinaryReader) : this(reader.readEnum<Hand>())

        override fun type(): ActionType = ActionType.INTERACT

        override fun write(writer: BinaryWriter) {
            writer.writeEnum(hand)
        }
    }

    object AttackAction : Action {

        override fun type(): ActionType = ActionType.ATTACK

        override fun write(writer: BinaryWriter) {
            // Nothing to write for the attack action
        }
    }

    @JvmRecord
    data class InteractAtAction(val x: Float, val y: Float, val z: Float, val hand: Hand) : Action {

        constructor(reader: BinaryReader) : this(reader.readFloat(), reader.readFloat(), reader.readFloat(), reader.readEnum<Hand>())

        override fun type(): ActionType = ActionType.INTERACT_AT

        override fun write(writer: BinaryWriter) {
            writer.writeFloat(x)
            writer.writeFloat(y)
            writer.writeFloat(z)
            writer.writeEnum(hand)
        }
    }

    enum class ActionType(private val reader: Reader) {

        INTERACT({ InteractAction(it) }),
        ATTACK({ AttackAction }),
        INTERACT_AT({ InteractAtAction(it) });

        fun read(reader: BinaryReader): Action = this.reader.read(reader)

        private fun interface Reader {

            fun read(reader: BinaryReader): Action
        }
    }
}
