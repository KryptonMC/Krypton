/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.event.ComponentResult
import org.kryptonmc.api.event.ResultedEvent
import java.net.InetSocketAddress
import java.util.UUID

/**
 * Called when a player has been authenticated, but they have not yet had
 * a player object constructed for them.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface LoginEvent : ResultedEvent<ComponentResult> {

    /**
     * The username of the player logging in.
     */
    @get:JvmName("username")
    public val username: String

    /**
     * The unique ID of the player logging in.
     */
    @get:JvmName("uuid")
    public val uuid: UUID

    /**
     * The address that the player is logging in from.
     */
    @get:JvmName("address")
    public val address: InetSocketAddress
}
