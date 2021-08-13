/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.command.meta.SimpleCommandMeta

/**
 * The command manager is responsible for registering, unregistering, and
 * keeping track of [Command]s
 */
interface CommandManager {

    /**
     * Registers a Brigadier command with this manager.
     *
     * @param command the command to register
     */
    fun register(command: BrigadierCommand)

    /**
     * Registers the given simple [command] with the given [meta] to
     * this manager.
     *
     * @param command the command to register
     * @param meta the command metadata
     */
    fun register(command: SimpleCommand, meta: SimpleCommandMeta)

    /**
     * Registers the given raw [command] with the given [meta] to
     * this manager.
     *
     * @param command the command to register
     * @param meta the command metadata
     */
    fun register(command: RawCommand, meta: CommandMeta)

    /**
     * Registers the given [command] with the given [meta] to this manager using
     * the given [registrar].
     *
     * @param command the command to register
     * @param meta the command metadata
     * @param registrar the command registrar to use to register the command
     */
    fun <C : Command, M : CommandMeta> register(command: C, meta: M, registrar: CommandRegistrar<C, M>)

    /**
     * Unregisters the given alias from this manager, if registered.
     *
     * @param alias the alias to unregister
     */
    fun unregister(alias: String)

    /**
     * Dispatches a command from the given [sender] with the given [command]
     * as the input.
     *
     * @param sender the sender of the command
     * @param command the command to dispatch
     * @return true if the command dispatched successfully, false otherwise
     */
    fun dispatch(sender: Sender, command: String): Boolean
}
