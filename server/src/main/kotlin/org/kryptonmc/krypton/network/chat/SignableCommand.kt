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
