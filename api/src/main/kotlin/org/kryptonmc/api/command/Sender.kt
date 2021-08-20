/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identified
import org.kryptonmc.api.Server
import org.kryptonmc.api.permission.Subject

/**
 * A sender is an interface representing the sender of a command.
 */
interface Sender : Audience, Subject, Identified {

    /**
     * The name of the sender.
     */
    val name: String

    /**
     * The sender's permission level (the equivalent to Minecraft's operator level).
     */
    val permissionLevel: Int

    /**
     * The sender's server
     */
    val server: Server
}
