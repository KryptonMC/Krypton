/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import com.google.common.collect.ImmutableList

/**
 * A command that may be invoked with arbitrary arguments.
 *
 * @param A the type of the arguments
 */
public sealed interface InvocableCommand<A> : Command {

    /**
     * Invokes this command with the given [sender] and [args].
     *
     * This will be called by the command manager when this command is invoked.
     *
     * A command is defined as being invoked when a [Sender] executes it
     * through some medium, such as typing it out in chat, or in the console.
     *
     * @param sender the sender who ran this command
     * @param args the arguments provided by the sender
     */
    public fun execute(sender: Sender, args: A)

    /**
     * Gets the list of suggestions for the given [sender] and the given
     * [args].
     *
     * This will be called by the command manager when suggestions are
     * requested for this command.
     *
     * @param sender the sender who sent the tab completion request
     * @param args the arguments provided by the sender
     * @return a list of possible tab completions
     */
    public fun suggest(sender: Sender, args: A): List<String> = ImmutableList.of()

    /**
     * Checks if the given [sender] has permission to execute this command with
     * the given [args].
     *
     * @param sender the sender attempting to execute the command
     * @param args the arguments provided by the sender
     * @return true if the sender can perform this command with the arguments,
     * false otherwise
     */
    public fun hasPermission(sender: Sender, args: A): Boolean = true
}
