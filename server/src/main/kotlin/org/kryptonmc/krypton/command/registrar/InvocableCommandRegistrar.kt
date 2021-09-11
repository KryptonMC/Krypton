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
import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.command.InvocableCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.buildRawArgumentsLiteral
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.Lock

sealed class InvocableCommandRegistrar<C : InvocableCommand<A>, M : CommandMeta, A>(lock: Lock) :
    KryptonCommandRegistrar<C, M>(lock) {

    abstract fun execute(command: C, meta: M, context: CommandContext<Sender>): Int

    abstract fun suggest(
        command: C,
        meta: M,
        context: CommandContext<Sender>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions>

    override fun register(root: RootCommandNode<Sender>, command: C, meta: M) {
        val name = meta.name.lowercase()
        val node = buildRawArgumentsLiteral(
            name,
            { execute(command, meta, it) },
            { context, builder -> suggest(command, meta, context, builder) }
        )
        register(root, node)
        meta.aliases.forEach {
            val value = it.lowercase()
            if (value == name) return@forEach
            register(root, node, value)
        }
    }
}
