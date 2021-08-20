/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.command.registrar

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.command.RawCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.rawArguments
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.Lock

class RawCommandRegistrar(lock: Lock) : InvocableCommandRegistrar<RawCommand, CommandMeta, String>(lock) {

    override fun execute(command: RawCommand, meta: CommandMeta, context: CommandContext<Sender>): Int {
        command.execute(context.source, context.rawArguments)
        return 1
    }

    override fun suggest(
        command: RawCommand,
        meta: CommandMeta,
        context: CommandContext<Sender>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        command.suggest(context.source, context.rawArguments).forEach { builder.suggest(it) }
        return builder.buildFuture()
    }
}
