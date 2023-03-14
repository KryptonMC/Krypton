/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.event.command

import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a command is executed by any sender.
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
