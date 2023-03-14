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
