/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import org.kryptonmc.api.Server
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.World
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The context of a command execution.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface CommandExecutionContext {

    /**
     * The sender of the command.
     */
    @get:JvmName("sender")
    public val sender: Sender

    /**
     * The position of the sender.
     */
    @get:JvmName("position")
    public val position: Vec3d

    /**
     * The world the sender is in.
     */
    @get:JvmName("world")
    public val world: World

    /**
     * The server the sender is on.
     */
    @get:JvmName("server")
    public val server: Server
}
