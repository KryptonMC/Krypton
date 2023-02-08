/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import net.kyori.adventure.text.Component
import org.kryptonmc.api.Server
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Position
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
    public val position: Position

    /**
     * The plain text name of the sender.
     */
    @get:JvmName("textName")
    public val textName: String

    /**
     * The display name of the sender.
     */
    @get:JvmName("displayName")
    public val displayName: Component

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

    /**
     * Checks if the sender is a player entity.
     *
     * If this returns true, [asPlayer] is guaranteed to return non-null.
     *
     * @return true if the sender is a player
     */
    public fun isPlayer(): Boolean

    /**
     * Gets the sender of the command as a player.
     *
     * @return the sender as a player
     */
    public fun asPlayer(): Player?

    /**
     * Sends the given [message] as a success message to the sender.
     *
     * @param message the message to send
     */
    public fun sendSuccessMessage(message: Component)

    /**
     * Sends the given [message] as a failure message to the sender.
     *
     * @param message the message to send
     */
    public fun sendFailureMessage(message: Component)
}
