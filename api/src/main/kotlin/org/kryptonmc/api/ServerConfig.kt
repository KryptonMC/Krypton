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
