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
package org.kryptonmc.krypton.network.chat

import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.context.CommandContextBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import org.kryptonmc.krypton.command.argument.SignedArgument

@JvmRecord
data class SignableCommand<S>(val arguments: List<Argument<S>>) {

    @JvmRecord
    data class Argument<S>(val node: ArgumentCommandNode<S, *>, val value: String) {

        fun name(): String = node.name
    }

    companion object {

        @JvmStatic
        fun <S> of(results: ParseResults<S>): SignableCommand<S> {
            val text = results.reader.string
            val context = results.context
            var currentContext: CommandContextBuilder<S> = context

            val arguments = collectArguments(text, context)
            var childContext: CommandContextBuilder<S>?
            while (currentContext.child.also { childContext = it } != null) {
                val differentRoots = childContext!!.rootNode != context.rootNode
                if (!differentRoots) break
                arguments.addAll(collectArguments(text, childContext!!))
                currentContext = childContext!!
            }
            return SignableCommand(arguments)
        }

        @JvmStatic
        private fun <S> collectArguments(text: String, context: CommandContextBuilder<S>): MutableList<Argument<S>> {
            val result = ArrayList<Argument<S>>()
            context.nodes.forEach {
                val node = it.node
                if (node !is ArgumentCommandNode<S, *> || node.type !is SignedArgument<*>) return@forEach
                val argument = context.arguments.get(node.name)
                if (argument != null) result.add(Argument(node, argument.range.get(text)))
            }
            return result
        }
    }
}
