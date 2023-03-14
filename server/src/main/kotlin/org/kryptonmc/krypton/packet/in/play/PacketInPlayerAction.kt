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
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readBlockPos
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeBlockPos

@JvmRecord
data class PacketInPlayerAction(
    val action: Action,
    val position: Vec3i,
    val direction: Direction,
    val sequence: Int
) : InboundPacket<PlayPacketHandler> {

    constructor(buf: ByteBuf) : this(buf.readEnum<Action>(), buf.readBlockPos(), buf.readEnum(), buf.readVarInt())

    override fun write(buf: ByteBuf) {
        buf.writeEnum(action)
        buf.writeBlockPos(position)
        buf.writeEnum(direction)
        buf.writeVarInt(sequence)
    }

    override fun handle(handler: PlayPacketHandler) {
        handler.handlePlayerAction(this)
    }

    enum class Action {

        START_DIGGING,
        CANCEL_DIGGING,
        FINISH_DIGGING,
        DROP_ITEM_STACK,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_ITEM_IN_HAND
    }
}
