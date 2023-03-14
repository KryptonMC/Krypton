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

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.CommandNode

class KryptonArgumentBuilder<S, T> private constructor(
    private val name: String,
    private val type: ArgumentType<T>
) : ArgumentBuilder<S, KryptonArgumentBuilder<S, T>>() {

    private var suggestionsProvider: SuggestionProvider<S>? = null

    fun suggests(provider: SuggestionProvider<S>?): KryptonArgumentBuilder<S, T> = apply { suggestionsProvider = provider }

    override fun then(argument: ArgumentBuilder<S, *>?): KryptonArgumentBuilder<S, T> {
        throw UnsupportedOperationException("Cannot add children to a greedy node!")
    }

    override fun then(argument: CommandNode<S>?): KryptonArgumentBuilder<S, T> {
        throw UnsupportedOperationException("Cannot add children to a greedy node!")
    }

    override fun getThis(): KryptonArgumentBuilder<S, T> = this

    override fun build(): KryptonArgumentCommandNode<S, T> =
        KryptonArgumentCommandNode(name, type, command, requirement, contextRequirement, redirect, redirectModifier, isFork, suggestionsProvider)

    companion object {

        @JvmStatic
        fun <S, T> kryptonArgument(name: String, type: ArgumentType<T>): KryptonArgumentBuilder<S, T> = KryptonArgumentBuilder(name, type)
    }
}
