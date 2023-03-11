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

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketInInteract(val entityId: Int, val action: Action, val sneaking: Boolean) : InboundPacket<PlayPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readEnum<ActionType>().read(buf), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeEnum(action.type())
        action.write(buf)
        buf.writeBoolean(sneaking)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handleInteract(this)
    }

    sealed interface Action : Writable {

        fun type(): ActionType
    }

    @JvmRecord
    data class InteractAction(val hand: Hand) : Action {

        constructor(buf: ByteBuf) : this(buf.readEnum<Hand>())

        override fun type(): ActionType = ActionType.INTERACT

        override fun write(buf: ByteBuf) {
            buf.writeEnum(hand)
        }
    }

    object AttackAction : Action {

        override fun type(): ActionType = ActionType.ATTACK

        override fun write(buf: ByteBuf) {
            // Nothing to write for the attack action
        }
    }

    @JvmRecord
    data class InteractAtAction(val x: Float, val y: Float, val z: Float, val hand: Hand) : Action {

        constructor(buf: ByteBuf) : this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readEnum<Hand>())

        override fun type(): ActionType = ActionType.INTERACT_AT

        override fun write(buf: ByteBuf) {
            buf.writeFloat(x)
            buf.writeFloat(y)
            buf.writeFloat(z)
            buf.writeEnum(hand)
        }
    }

    enum class ActionType(private val reader: Reader) {

        INTERACT({ InteractAction(it) }),
        ATTACK({ AttackAction }),
        INTERACT_AT({ InteractAtAction(it) });

        fun read(buf: ByteBuf): Action = reader.read(buf)

        private fun interface Reader {

            fun read(buf: ByteBuf): Action
        }
    }
}
