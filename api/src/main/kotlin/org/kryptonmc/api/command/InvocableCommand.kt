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
 * A command that may be invoked with arbitrary arguments.
 *
 * @param A the type of the arguments
 */
public interface InvocableCommand<A> : Command {

    /**
     * Invokes this command with the given [sender] and [args].
     *
     * This will be called by the command manager when this command is invoked.
     *
     * @param sender the sender who ran this command
     * @param args the arguments the sender sent
     */
    public fun execute(sender: Sender, args: A)

    /**
     * Gets the list of suggestions for the given [sender] and the given [args].
     *
     * This will be called by the command manager when suggestions are requested
     * for this command.
     *
     * @param sender the sender who sent the tab completion request
     * @param args the arguments the sender sent
     * @return a list of possible tab completions
     */
    public fun suggest(sender: Sender, args: A): List<String> = emptyList()
}
