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
sealed interface InvocableCommand<A> : Command {

    /**
     * Called when this command is executed (when the [sender] runs the command)
     *
     * @param sender the sender who ran this command
     * @param args the arguments the sender sent
     */
    fun execute(sender: Sender, args: A)

    /**
     * Called when the [sender] sends a tab complete request.
     *
     * @param sender the sender who sent the tab completion request
     * @param args the arguments the sender sent
     * @return a list of possible tab completions
     */
    fun suggest(sender: Sender, args: A): List<String> = emptyList()
}
