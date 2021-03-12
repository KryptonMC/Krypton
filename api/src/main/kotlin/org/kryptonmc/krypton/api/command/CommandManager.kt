package org.kryptonmc.krypton.api.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder

/**
 * The command manager is responsible for registering, unregistering, and
 * keeping track of [Command]s
 *
 * @author Callum Seabrook
 */
interface CommandManager {

    /**
     * Register a command
     *
     * @param command the command to register
     */
    fun register(command: Command)

    /**
     * Register commands
     *
     * @param commands the commands to register
     */
    fun register(vararg commands: Command)

    /**
     * Register commands
     *
     * @param commands the commands to register
     */
    fun register(commands: Iterable<Command>)

    /**
     * Register a Brigadier [com.mojang.brigadier.Command]
     *
     * @param command the command to register
     */
    fun register(command: LiteralArgumentBuilder<Sender>)

    /**
     * Register Brigadier [com.mojang.brigadier.Command]s
     *
     * @param commands the commands to register
     */
    fun register(vararg commands: LiteralArgumentBuilder<Sender>)

    /**
     * Dispatch a command from the given [sender] with the given [command]
     * as the input.
     *
     * @param sender the sender of the command
     * @param command the command to dispatch
     */
    fun dispatch(sender: Sender, command: String)
}