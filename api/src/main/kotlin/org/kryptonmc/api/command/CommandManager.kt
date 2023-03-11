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

import org.kryptonmc.api.entity.player.Player

/**
 * The command manager is responsible for registering, unregistering, and
 * keeping track of commands.
 */
public interface CommandManager {

    /**
     * Registers the given Brigadier [command] with the given [meta] to this
     * manager.
     *
     * @param command the command to register
     * @param meta the command metadata
     */
    public fun register(command: BrigadierCommand, meta: CommandMeta)

    /**
     * Registers the given invocable [command] with the given [meta] to this
     * manager.
     *
     * @param command the command to register
     * @param meta the command metadata
     */
    public fun register(command: InvocableCommand<*>, meta: CommandMeta)

    /**
     * Unregisters the given alias from this manager, if registered.
     *
     * @param alias the alias to unregister
     */
    public fun unregister(alias: String)

    /**
     * Dispatches a command from the given [sender] with the given [command]
     * as the input.
     *
     * @param sender the sender of the command
     * @param command the command to dispatch
     * @return true if the command dispatched successfully, false otherwise
     */
    public fun dispatch(sender: Sender, command: String): Boolean

    /**
     * Updates the list of known commands for the given [player].
     *
     * This is useful for permission plugins that want to trigger a refresh of
     * commands when a user gains or loses a permission.
     *
     * @param player the player to update commands for
     */
    public fun updateCommands(player: Player)
}
