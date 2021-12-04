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
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.command.SimpleCommand
import org.kryptonmc.api.command.meta.SimpleCommandMeta
import org.kryptonmc.krypton.command.splitArguments
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.Lock

class SimpleCommandRegistrar(lock: Lock) : InvocableCommandRegistrar<SimpleCommand, SimpleCommandMeta, Array<String>>(lock) {

    override fun execute(command: SimpleCommand, meta: SimpleCommandMeta, context: CommandContext<Sender>): Int {
        val sender = context.source
        val args = context.splitArguments()
        if (meta.permission == null) return dispatch(command, sender, args)

        return when (sender.getPermissionValue(meta.permission!!)) {
            TriState.TRUE -> dispatch(command, sender, args)
            TriState.FALSE, TriState.NOT_SET -> 0
        }
    }

    override fun suggest(
        command: SimpleCommand,
        meta: SimpleCommandMeta,
        context: CommandContext<Sender>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val args = context.splitArguments()
        val hasPermission = meta.permission?.let(context.source::hasPermission) ?: true
        if (!hasPermission) return builder.buildFuture()

        command.suggest(context.source, args).forEach { builder.suggest(it) }
        return builder.buildFuture()
    }

    private fun dispatch(command: SimpleCommand, sender: Sender, args: Array<String>): Int {
        command.execute(sender, args)
        return 1
    }
}
