/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.command.InvocableCommand
import org.kryptonmc.api.command.Sender
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.Lock

/**
 * Registers commands that can be invoked. This is an abstraction over
 * [SimpleCommandRegistrar] and [RawCommandRegistrar], which adds in some
 * helper functions that we can just override in those subclasses to provide
 * the functionality that we need for each.
 */
sealed class InvocableCommandRegistrar<C : InvocableCommand<A>, M : CommandMeta, A>(lock: Lock) : KryptonCommandRegistrar<C, M>(lock) {

    abstract fun execute(command: C, meta: M, context: CommandContext<Sender>): Int

    abstract fun suggest(command: C, meta: M, context: CommandContext<Sender>, builder: SuggestionsBuilder): CompletableFuture<Suggestions>

    /**
     * This came from CraftBukkit. We create a copy of the suggestions builder
     * that only contains suggestions from the last
     * [argument separator][CommandDispatcher.ARGUMENT_SEPARATOR_CHAR] in the
     * input, so that we don't end up suggesting arguments after the first one
     * in place of the first one.
     *
     * For example, the way this was found was with `/lp user <username>`.
     * Completing `user` worked fine, but completing the `<username>` would
     * replace `user` with the `<username>`, meaning we would get
     * `/lp <username>`.
     */
    fun createSuggestions(builder: SuggestionsBuilder, results: List<String>): CompletableFuture<Suggestions> {
        val offsetBuilder = builder.createOffset(builder.input.lastIndexOf(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) + 1)
        results.forEach(offsetBuilder::suggest)
        return offsetBuilder.buildFuture()
    }

    override fun register(root: RootCommandNode<Sender>, command: C, meta: M) {
        val name = meta.name.lowercase()
        val node = buildRawArgumentsLiteral(name, { execute(command, meta, it) }, { context, builder -> suggest(command, meta, context, builder) })
        register(root, node)
        meta.aliases.forEach {
            val value = it.lowercase()
            if (value == name) return@forEach
            register(root, node, value)
        }
    }

    companion object {

        @JvmStatic
        private fun buildRawArgumentsLiteral(
            alias: String,
            command: Command<Sender>,
            suggestionProvider: SuggestionProvider<Sender>
        ): LiteralCommandNode<Sender> = LiteralArgumentBuilder.literal<Sender>(alias.lowercase())
            .then(RequiredArgumentBuilder.argument<Sender, String>("arguments", StringArgumentType.greedyString())
                .suggests(suggestionProvider)
                .executes(command))
            .executes(command)
            .build()
    }
}
