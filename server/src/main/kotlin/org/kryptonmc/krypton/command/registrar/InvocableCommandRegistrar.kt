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
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContextBuilder
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.InvocableCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.brigadier.KryptonArgumentBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.locks.Lock
import java.util.function.Predicate

/**
 * Registers commands that can be invoked. This is an abstraction over
 * [SimpleCommandRegistrar] and [RawCommandRegistrar], which adds in some
 * helper functions that we can just override in those subclasses to provide
 * the functionality that we need for each.
 */
abstract class InvocableCommandRegistrar<C : IC<A>, A>(lock: Lock, private val argumentsType: ArgumentType<A>) : AbstractCommandRegistrar<C>(lock) {

    protected abstract fun getArgs(arguments: Map<String, ParsedArgument<*, *>>): A

    protected inline fun <reified V> readArguments(arguments: Map<String, ParsedArgument<*, *>>, fallback: V): V {
        val argument = arguments.get("arguments") ?: return fallback
        try {
            return V::class.java.cast(argument.result)
        } catch (exception: ClassCastException) {
            throw IllegalArgumentException("Expected parsed argument to be of type ${V::class.java}, was ${argument.result.javaClass}!", exception)
        }
    }

    override fun register(root: RootCommandNode<CommandSourceStack>, command: C, meta: CommandMeta) {
        val literal = createLiteral(command, meta.name)
        register(root, literal)
        meta.aliases.forEach { register(root, literal, it) }
    }

    private fun createLiteral(command: C, alias: String): LiteralCommandNode<CommandSourceStack> {
        val requirement = Predicate<CommandContextBuilder<CommandSourceStack>> { command.hasPermission(it.source.sender, getArgs(it.arguments)) }
        val callback = Command<CommandSourceStack> {
            command.execute(it.source.sender, getArgs(it.arguments))
            1
        }
        val literal = LiteralArgumentBuilder.literal<CommandSourceStack>(alias)
            .requiresWithContext { context, reader -> reader.canRead() || requirement.test(context) }
            .executes(callback)
            .build()
        val arguments = KryptonArgumentBuilder.kryptonArgument<CommandSourceStack, A>("arguments", argumentsType)
            .requiresWithContext { context, _ -> requirement.test(context) }
            .executes(callback)
            .suggests { context, builder -> createSuggestions(builder, command.suggest(context.source.sender, getArgs(context.arguments))) }
            .build()
        literal.addChild(arguments)
        return literal
    }

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
    private fun createSuggestions(builder: SuggestionsBuilder, results: List<String>): CompletableFuture<Suggestions> {
        val offsetBuilder = builder.createOffset(builder.input.lastIndexOf(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) + 1)
        results.forEach(offsetBuilder::suggest)
        return offsetBuilder.buildFuture()
    }
}

private typealias IC<A> = InvocableCommand<A>
