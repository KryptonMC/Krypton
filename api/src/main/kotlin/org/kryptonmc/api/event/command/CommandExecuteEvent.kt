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
import javax.annotation.concurrent.Immutable

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
     * @param command the command to forward
     */
    @JvmRecord
    @Immutable
    public data class Result(override val isAllowed: Boolean, public val command: String? = null) : ResultedEvent.Result {

        public companion object {

            private val ALLOWED = Result(true, null)
            private val DENIED = Result(false, null)

            /**
             * Returns a result that allows the command without any modifications,
             * and without forwarding it to the backend server.
             *
             * @return the allowed result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun allowed(): Result = ALLOWED

            /**
             * Returns a result that denies the command from being executed by the
             * server.
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
