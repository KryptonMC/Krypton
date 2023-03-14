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
package org.kryptonmc.krypton.packet.`in`.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.handlers.StatusPacketHandler
import org.kryptonmc.krypton.packet.InboundPacket

/**
 * Sent by the client to request the server's status information.
 */
object PacketInStatusRequest : InboundPacket<StatusPacketHandler> {

    override fun write(buf: ByteBuf) {
        // there is nothing to write here, and nothing is read for this either
    }

    override fun handle(handler: StatusPacketHandler) {
        handler.handleStatusRequest()
    }
}
