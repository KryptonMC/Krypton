/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import org.kryptonmc.api.entity.player.Player

/**
 * The command manager is responsible for registering, unregistering, and
 * keeping track of [Command]s
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
     * Registers the given [command] with the given [meta] to this manager using
     * the given [registrar].
     *
     * This is designed
     *
     * @param command the command to register
     * @param meta the command metadata
     * @param registrar the command registrar to use to register the command
     */
    public fun <C, M> register(command: C, meta: M, registrar: CommandRegistrar<C, M>)

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
