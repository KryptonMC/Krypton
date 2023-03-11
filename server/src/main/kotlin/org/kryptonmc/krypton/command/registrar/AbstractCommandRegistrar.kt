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
package org.kryptonmc.krypton.command.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.Command
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.krypton.command.CommandSourceStack
import java.util.concurrent.locks.Lock
import kotlin.concurrent.withLock

abstract class AbstractCommandRegistrar<C : Command>(private val lock: Lock) {

    abstract fun register(root: RootCommandNode<CommandSourceStack>, command: C, meta: CommandMeta)

    protected fun register(root: RootCommandNode<CommandSourceStack>, node: LiteralCommandNode<CommandSourceStack>) {
        lock.withLock {
            root.removeChildByName(node.name)
            root.addChild(node)
        }
    }

    protected fun register(root: RootCommandNode<CommandSourceStack>, node: LiteralCommandNode<CommandSourceStack>, alias: String) {
        register(root, copyRedirect(node, alias))
    }

    companion object {

        @JvmStatic
        private fun copyRedirect(source: LiteralCommandNode<CommandSourceStack>, redirectAlias: String): LiteralCommandNode<CommandSourceStack> {
            val builder = LiteralArgumentBuilder.literal<CommandSourceStack>(redirectAlias)
                .requires(source.requirement)
                .requiresWithContext(source.contextRequirement)
                .forward(source.redirect, source.redirectModifier, source.isFork)
                .executes(source.command)
            source.children.forEach { builder.then(it) }
            return builder.build()
        }
    }
}
