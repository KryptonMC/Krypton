/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.handshake

import org.kryptonmc.api.event.Event
import java.net.InetSocketAddress

/**
 * Called when a connection is made and a handshake packet is sent.
 *
 * There is no player data sent by this time, as this packet may either
 * indicate the client intends to login, or they intend to request the
 * status information for the server.
 *
 * @param address the address of the connecting client
 */
class HandshakeEvent(val address: InetSocketAddress) : Event
