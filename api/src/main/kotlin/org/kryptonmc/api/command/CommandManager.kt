/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

/**
 * The command manager is responsible for registering, unregistering, and
 * keeping track of [Command]s
 */
interface CommandManager {

    /**
     * Register a Brigadier command with this manager.
     *
     * @param command the command to register
     */
    fun register(command: BrigadierCommand)

    /**
     * Register a legacy command with this manager.
     *
     * @param command the command to register
     */
    fun register(command: SimpleCommand)

    /**
     * Register a raw command with this manager.
     *
     * @param command the command to register
     */
    fun register(command: RawCommand)

    /**
     * Dispatch a command from the given [sender] with the given [command]
     * as the input.
     *
     * @param sender the sender of the command
     * @param command the command to dispatch
     */
    fun dispatch(sender: Sender, command: String)
}
