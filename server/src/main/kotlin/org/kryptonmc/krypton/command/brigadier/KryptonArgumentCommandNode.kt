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
package org.kryptonmc.krypton.command.brigadier

import com.mojang.brigadier.Command
import com.mojang.brigadier.ImmutableStringReader
import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.CommandContextBuilder
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import java.util.concurrent.CompletableFuture
import java.util.function.BiPredicate
import java.util.function.Predicate

@Suppress("LongParameterList")
class KryptonArgumentCommandNode<S, T>(
    name: String?,
    private val richType: ArgumentType<T>,
    command: Command<S>?,
    requirement: Predicate<S>?,
    contextRequirement: BiPredicate<CommandContextBuilder<S>, ImmutableStringReader>?,
    redirect: CommandNode<S>?,
    modifier: RedirectModifier<S>?,
    forks: Boolean,
    customSuggestions: SuggestionProvider<S>?
) : ArgumentCommandNode<S, String>(name, greedyString(), command, requirement, contextRequirement, redirect, modifier, forks, customSuggestions) {

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder<S>) {
        val start = reader.cursor
        val result = richType.parse(reader)
        if (reader.canRead()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                .createWithContext(reader, "Expected greedy ArgumentType to parse all input!")
        }

        val parsed = ParsedArgument<S, T>(start, reader.cursor, result)
        contextBuilder.withArgument(name, parsed)
        contextBuilder.withNode(this, parsed.range)
    }

    override fun listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        if (customSuggestions == null) return Suggestions.empty()
        return customSuggestions.getSuggestions(context, builder)
    }

    override fun createBuilder(): RequiredArgumentBuilder<S, String> {
        throw UnsupportedOperationException()
    }

    override fun isValidInput(input: String?): Boolean = true

    override fun addChild(node: CommandNode<S>?) {
        throw UnsupportedOperationException("Cannot add children to a greedy node!")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KryptonArgumentCommandNode<*, *> || !super.equals(other)) return false
        return richType == other.richType
    }

    override fun hashCode(): Int = 31 * super.hashCode() + richType.hashCode()

    override fun getExamples(): MutableCollection<String> = richType.examples

    override fun toString(): String = "<argument $name:$richType>"
}
