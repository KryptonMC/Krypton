/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import net.kyori.adventure.audience.Audience
import org.kryptonmc.api.Server
import org.kryptonmc.api.permission.Subject

/**
 * A sender is an interface representing the sender of a command.
 */
public interface Sender : Audience, Subject {

    /**
     * The name of the sender.
     *
     * How this is defined is entirely dependent on the subtype.
     */
    public val name: String

    /**
     * The server that the sender is on.
     */
    public val server: Server
}
