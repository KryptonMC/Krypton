/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.command

import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when the given [command] is executed by the given [sender].
 */
public interface CommandExecuteEvent : DeniableEventWithResult<CommandExecuteEvent.Result> {

    /**
     * The sender that executed the command.
     */
    public val sender: Sender

    /**
     * The command that was executed.
     */
    public val command: String

    /**
     * The result of a command's execution.
     *
     * This can be used to completely replace the command that the player
     * executed, which can be useful for redirecting commands to others.
     *
     * @property command The replacement command to execute.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val command: String)
}
