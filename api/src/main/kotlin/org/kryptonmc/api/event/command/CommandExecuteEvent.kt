/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.command

import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when the given [command] is executed by the given [sender].
 *
 * @param sender the sender that executed the command
 * @param command the command that was executed
 */
public class CommandExecuteEvent(
    public val sender: Sender,
    public val command: String
) : ResultedEvent<CommandResult> {

    override var result: CommandResult = CommandResult.allowed()
}

/**
 * The result of a [CommandExecuteEvent].
 *
 * @param command the command to forward
 */
@JvmRecord
public data class CommandResult(
    override val isAllowed: Boolean,
    public val command: String? = null
) : ResultedEvent.Result {

    override fun toString(): String = if (isAllowed) "allowed" else "denied"

    public companion object {

        private val ALLOWED = CommandResult(true, null)
        private val DENIED = CommandResult(false, null)

        /**
         * Returns a result that allows the command without any modifications,
         * and without forwarding it to the backend server.
         *
         * @return the allowed result
         */
        @JvmStatic
        public fun allowed(): CommandResult = ALLOWED

        /**
         * Returns a result that denies the command from being executed by the
         * server.
         *
         * @return the denied result
         */
        @JvmStatic
        public fun denied(): CommandResult = DENIED

        /**
         * Creates a new result that allows the command to be executed, but
         * silently replaces it with the given [newCommand].
         *
         * @param newCommand the replacement command
         * @return a new allowed result
         */
        @JvmStatic
        public fun command(newCommand: String): CommandResult = CommandResult(true, newCommand)
    }
}
