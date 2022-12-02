/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import net.kyori.adventure.text.Component
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The basic configuration options associated with the server.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ServerConfig {

    /**
     * If the server is in online mode, meaning it authenticates players
     * through verified means. The authentication provider will, for most
     * implementations, be Mojang.
     */
    public val isOnline: Boolean

    /**
     * The address that the server is bound to.
     */
    @get:JvmName("ip")
    public val ip: String

    /**
     * The port that the server is bound to.
     */
    @get:JvmName("port")
    public val port: Int

    /**
     * The message of the day for the server's status.
     */
    @get:JvmName("motd")
    public val motd: Component

    /**
     * The maximum players that may join the server.
     */
    @get:JvmName("maxPlayers")
    public val maxPlayers: Int
}
