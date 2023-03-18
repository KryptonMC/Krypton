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
package org.kryptonmc.krypton.network.interceptor

import org.kryptonmc.krypton.network.NioConnection
import org.kryptonmc.krypton.network.handlers.PacketHandler
import org.kryptonmc.krypton.packet.GenericPacket
import org.kryptonmc.krypton.packet.InboundPacket

/**
 * This interface is used to enable the interception of packets, both inbound and outbound.
 *
 * There are two hooks present in this interface, [onSend] and [onReceive]. These hooks are called when
 * their respective events happen, e.g. onSend when a packet is sent.
 */
interface PacketInterceptor {

    /**
     * Called when a packet is sent to a client.
     *
     * This works in a specific way, with the following rules:
     * - If you do not wish to modify the packet, return it.
     * - If you do not want the packet to be sent, return `null`.
     * - If you want to modify the packet, return the modified packet.
     */
    fun onSend(connection: NioConnection, packet: GenericPacket): GenericPacket?

    /**
     * Called when a packet is received from a client.
     *
     * This works in a specific way, with the following rules:
     * - If you do not wish to modify the packet, return it.
     * - If you do not want the packet to be processed, return `null`.
     * - If you want to modify the packet, return the modified packet.
     */
    fun <H : PacketHandler> onReceive(connection: NioConnection, packet: InboundPacket<H>): InboundPacket<H>?
}
