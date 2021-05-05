/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.command

/**
 * Represents a command that may be executed, or provide suggestions to the user.
 *
 * @param name the name of the command
 * @param permission the permission required to execute the command (`null` for no permission)
 * @param aliases a list of aliases for this command
 */
abstract class Command @JvmOverloads constructor(
    val name: String,
    val permission: String? = null,
    val aliases: List<String> = emptyList()
) {

    /**
     * Called when this command is executed (when the [sender] runs the command)
     *
     * @param sender the sender who ran this command
     * @param args the arguments supplied to this command
     */
    abstract fun execute(sender: Sender, args: List<String>)

    /**
     * Called when the [sender] sends a tab complete request.
     *
     * @param sender the sender who sent the tab completion request
     * @param args the arguments the sender sent
     * @return a list of possible tab completions for the specified [args]
     */
    open fun suggest(sender: Sender, args: List<String>): List<String> = emptyList()
}
