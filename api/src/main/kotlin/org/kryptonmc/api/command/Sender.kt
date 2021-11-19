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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.Server
import org.kryptonmc.api.permission.Subject
import java.util.UUID

/**
 * A sender is an interface representing the sender of a command.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Sender : Audience, Subject, Identified {

    /**
     * The name of the sender.
     *
     * How this is defined is entirely dependent on the subtype.
     */
    @get:JvmName("name")
    public val name: Component

    /**
     * The UUID of this sender.
     */
    @get:JvmName("uuid")
    public val uuid: UUID

    /**
     * The server that the sender is on.
     */
    @get:JvmName("server")
    public val server: Server
}
