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
