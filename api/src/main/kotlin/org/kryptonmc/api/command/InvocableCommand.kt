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
