/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.command

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when the given [command] is executed by the given [sender].
 */
public interface CommandExecuteEvent : ResultedEvent<CommandExecuteEvent.Result> {

    /**
     * The sender that executed the command.
     */
    public val sender: Sender

    /**
     * The command that was executed.
     */
    public val command: String

    /**
     * The result of a [CommandExecuteEvent].
     *
     * @property isAllowed Whether the command is allowed to be executed.
     * @property command The replacement command to execute, or null, if the
     * original command should be executed.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(override val isAllowed: Boolean, public val command: String?) : ResultedEvent.Result {

        public companion object {

            private val ALLOWED = Result(true, null)
            private val DENIED = Result(false, null)

            /**
             * Gets the result that allows the command to be executed without
             * any modifications.
             *
             * @return the allowed result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun allowed(): Result = ALLOWED

            /**
             * Gets the result that denies the command from being executed by
             * the server.
             *
             * @return the denied result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun denied(): Result = DENIED

            /**
             * Creates a new result that allows the command to be executed, but
             * silently replaces it with the given [newCommand].
             *
             * @param newCommand the replacement command
             * @return a new allowed result
             */
            @JvmStatic
            @Contract("_ -> new", pure = true)
            public fun command(newCommand: String): Result = Result(true, newCommand)
        }
    }
}
