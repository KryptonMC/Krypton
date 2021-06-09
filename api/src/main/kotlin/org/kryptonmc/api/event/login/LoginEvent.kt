/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.login

import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.event.ComponentResult
import org.kryptonmc.api.event.ResultedEvent
import java.net.InetSocketAddress
import java.util.UUID

/**
 * Called when a player has been authenticated, but they have not yet had
 * a player object constructed for them (when the state has not moved into PLAY
 * yet).
 *
 * @param username the username of the player logging in
 * @param uuid the UUID of the player logging in
 * @param address the address of the player logging in
 */
class LoginEvent(
    val username: String,
    val uuid: UUID,
    val address: InetSocketAddress
) : ResultedEvent<ComponentResult> {

    override var result = ComponentResult.allowed(translatable("multiplayer.disconnect.kicked"))
}
